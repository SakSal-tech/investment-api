package com.sakhiya.investment.portfoliomanagement.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import com.sakhiya.investment.portfoliomanagement.asset.marketdata.AlphaVantageClient;
import com.sakhiya.investment.portfoliomanagement.asset.marketdata.AlphaVantagePriceDTO;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;

@RestController
@RequestMapping("/api/asset-price-history")
public class AssetPriceHistoryController {

    @Autowired
    private AssetRepository assetRepository;
    // @Autowired field for AlphaVantageClient to fetch external price data
    @Autowired
    private AlphaVantageClient alphaVantageClient;

    // To handle the import and saving of price history data
    @Autowired
    private AssetPriceHistoryRepository priceHistoryRepository;

    // controller can use the service logic, while Spring handles the wiring
    // Follows the dependency injection principle, making code loosely coupled and easier to test
    @Autowired
    private AssetHistoryService assetHistoryService;

    // Refactoring and created a helper method to avoid repetition, I had same
    // code(limiting the price history from external API to be 7 days only to avoid
    // so much data is returned) appearing more than one method
    // now I can extract the date range calculation from helper method so other methods can call
    // It returns a LocalDate[] array so it can easily return both the start and end
    // dates together f
    private LocalDate[] getDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDate end;
        if (endDate != null) {
            end = endDate;
        } else {
            end = LocalDate.now();
        }

        LocalDate start;
        if (startDate != null) {
            start = startDate;
        } else {
            start = end.minusDays(6);
        }

        return new LocalDate[] { start, end };
    }

    // --- Another Refactoring: Helper method for mapping AssetPriceHistory to DTO ---
/**
 * Refactored to avoid repeated DTO mapping code in each GET endpoint.
  * Only exposes clean DTOs in API responses, preventing nested asset/portfolio/client data.
 */
private AssetPriceHistoryDTO toDTOHelper(AssetPriceHistory priceHistory) {
    return new AssetPriceHistoryDTO(
        priceHistory.getAsset().getAssetId(),
        priceHistory.getAsset().getName(),
        priceHistory.getTradingDate(),
        priceHistory.getClosingPrice(),
        
        priceHistory.getSource()
    );
}

    // Handles HTTP POST requests to /api/asset-price-history to add a new price
    // history record
    @PostMapping
    public AssetPriceHistory addPriceHistory(@RequestBody AssetPriceHistoryDTO dto) {
        // Create a new AssetPriceHistory entity object
        AssetPriceHistory priceHistory = new AssetPriceHistory();
        // Set the trading date from the DTO (data sent by the client)
        priceHistory.setTradingDate(dto.getTradingDate());
        // Set the closing price from the DTO
        priceHistory.setClosingPrice(dto.getClosingPrice());
        // Set the data source (e.g., AlphaVantage) from the DTO
        priceHistory.setSource(dto.getSource());
        // Fetch the Asset entity from the database using the assetId provided in the
        // DTO.
        // else if no assetID throw and exception
        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + dto.getAssetId()));
        // Link the AssetPriceHistory to the correct Asset
        priceHistory.setAsset(asset);
        // Save the new AssetPriceHistory entity to the database and return it
        return priceHistoryRepository.save(priceHistory);
    }

    // Get all price history records
    @GetMapping
    public List<AssetPriceHistoryDTO> getAllPriceHistories(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        //optional parameters, if user does not pass it will still retun 7 days history by calling getDateRange method
        LocalDate[] range = getDateRange(startDate, endDate);
        LocalDate start = range[0];
        LocalDate end = range[1];
        //Using a custom repository method for date filtering as findall() does not take parameters
        List<AssetPriceHistory> priceObs = priceHistoryRepository.findByTradingDateBetweenOrderByTradingDateAsc(start, end);
        return priceObs.stream().map(this::toDTOHelper).toList();
    }

    // Get price history by ID
    @GetMapping("/{id}")
    public AssetPriceHistoryDTO getPriceHistoryById(@PathVariable String id) {
        AssetPriceHistory priceHistory = priceHistoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Price history not found: " + id));
        // call the helper method
        return toDTOHelper(priceHistory);

    }

    // Update price history by ID
    @PutMapping("/{id}")
    public AssetPriceHistory updatePriceHistory(@PathVariable String id, @RequestBody AssetPriceHistoryDTO dto) {
        AssetPriceHistory history = priceHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Price history not found: " + id));
        history.setTradingDate(dto.getTradingDate());
        history.setClosingPrice(dto.getClosingPrice());
        history.setSource(dto.getSource());
        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + dto.getAssetId()));
        history.setAsset(asset);
        return priceHistoryRepository.save(history);
    }

    // Delete price history by ID
    @DeleteMapping("/{id}")
    public void deletePriceHistory(@PathVariable String id) {
        if (!priceHistoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Price history not found: " + id);
        }
        priceHistoryRepository.deleteById(id);
    }

    // Get all price history for a specific asset
    @GetMapping("/asset/{assetId}")
    /**
     * Returns a clean list of price history DTOs for the given asset, filtered by
     * date range.
     * Accepts optional startDate and endDate query parameters. Defaults to last 7
     * days if not provided.
     * returns List<AssetPriceHistoryDTO list of DTOs not entities.
     */
    public List<AssetPriceHistoryDTO> getPriceHistoryByAsset(
            @PathVariable String assetId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
            LocalDate[] range = getDateRange(startDate, endDate);
            LocalDate start = range[0];
            LocalDate end = range[1];
        List<AssetPriceHistory> entities = priceHistoryRepository
                .findByAsset_AssetIdAndTradingDateBetweenOrderByTradingDateAsc(assetId, start, end);
        return entities.stream().map(this::toDTOHelper).toList();

    }

    // Get all price history for a specific asset symbol (e.g., IBM)
    @GetMapping("/symbol/{symbol}")
    public List<AssetPriceHistoryDTO> getPriceHistoryBySymbol(@PathVariable String symbol) {
        List<AssetPriceHistory> pricHistoryeObs = priceHistoryRepository.findBySourceOrderByTradingDateAsc(symbol);
        return pricHistoryeObs.stream().map(this::toDTOHelper).toList();

    }

    /**
     * Import asset price history for a given asset and symbol, only for the last 7
     * days (including today).
     * This prevents importing excessive data and keeps the database focused on
     * recent prices.
     */
    @PostMapping("/import/{assetId}/{symbol}")
    public ResponseEntity<?> importFromAlphaVantage(
            @PathVariable String assetId,
            @PathVariable String symbol) {
        try {
            LocalDate end = LocalDate.now();// today's date
            LocalDate start = end.minusDays(6);// go back 6 days
            // create a list of alphaObjects contains DTOs built from the real data fetched
            // via the external API for specific symbol e.g IBM. calls client class, which
            // connects to the external AlphaVantage API and fetches the price data
            List<AlphaVantagePriceDTO> alphaObjects = alphaVantageClient.getDailyPriceDTOs(symbol);
            // Filter the DTOs to only include those within the last 7 days
            List<AlphaVantagePriceDTO> filtered = alphaObjects.stream()
                    // .filter keeps only the elements in the stream that match the condition inside
                    // the parentheses.dto is a lambda expression(foreach AlphaVantagePriceDTO         // object (named dto)).
                    // !dto.getTradingDate().isBefore(start) and isAfter(end), Check that the
                    // trading date is NOT before & after the start date.dto.getTradingDate(), gets
                    // the trading date from the current DTO.
                    .filter(dto -> !dto.getTradingDate().isBefore(start) && !dto.getTradingDate().isAfter(end))
                    .toList();
            // call the import method from service class
            assetHistoryService.importPriceHistoryFromAlphaVantage(assetId, filtered, symbol);
            return ResponseEntity.ok(filtered);
            // In case external AlphaVantage API is unreachable or returns an error or there
            // is a problem with filtering or processing the data or any other unexpected
            // runtime error
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to import price history: " + ex.getMessage());
        }
    }
}