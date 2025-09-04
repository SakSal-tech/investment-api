package com.sakhiya.investment.portfoliomanagement.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import com.sakhiya.investment.portfoliomanagement.asset.marketdata.AlphaVantageClient;
import com.sakhiya.investment.portfoliomanagement.asset.marketdata.AlphaVantagePriceDTO;

import java.util.List;

@RestController
@RequestMapping("/api/asset-price-history")
public class AssetPriceHistoryController {

    @Autowired
    private AssetRepository assetRepository;
    // @Autowired field for AlphaVantageClient to fetch external price data
    @Autowired
    private AlphaVantageClient alphaVantageClient;

    //To handle the import and saving of price history data
    @Autowired
    private AssetPriceHistoryRepository priceHistoryRepository;

    @Autowired
    private AssetHistoryService assetHistoryService;

    // (I) Handles HTTP POST requests to /api/asset-price-history to add a new price
    // history record
    @PostMapping
    public AssetPriceHistory addPriceHistory(@RequestBody AssetPriceHistoryDTO dto) {
        // (I) Create a new AssetPriceHistory entity object
        AssetPriceHistory priceHistory = new AssetPriceHistory();
        // (I) Set the trading date from the DTO (data sent by the client)
        priceHistory.setTradingDate(dto.getTradingDate());
        // (I) Set the closing price from the DTO
        priceHistory.setClosingPrice(dto.getClosingPrice());
        // (I) Set the data source (e.g., AlphaVantage) from the DTO
        priceHistory.setSource(dto.getSource());
        // (I) Fetch the Asset entity from the database using the assetId provided in
        // the DTO
        Asset asset = assetRepository.findById(dto.getAssetId())
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + dto.getAssetId()));
        // (I) Link the AssetPriceHistory to the correct Asset
        priceHistory.setAsset(asset);
        // (I) Save the new AssetPriceHistory entity to the database and return it
        return priceHistoryRepository.save(priceHistory);
    }

    // (I) Get all price history records
    @GetMapping
    public List<AssetPriceHistory> getAllPriceHistories() {
        return priceHistoryRepository.findAll();
    }

    // (I) Get price history by ID
    @GetMapping("/{id}")
    public AssetPriceHistory getPriceHistoryById(@PathVariable String id) {
        return priceHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Price history not found: " + id));
    }

    // (I) Update price history by ID
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

    // (I) Delete price history by ID
    @DeleteMapping("/{id}")
    public void deletePriceHistory(@PathVariable String id) {
        if (!priceHistoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Price history not found: " + id);
        }
        priceHistoryRepository.deleteById(id);
    }

    // (I) Get all price history for a specific asset
    @GetMapping("/asset/{assetId}")
    /**
     * Returns a clean list of price history DTOs for the given asset, filtered by date range.
     * Accepts optional startDate and endDate query parameters. Defaults to last 7 days if not provided.
     */
    public List<AssetPriceHistoryDTO> getPriceHistoryByAsset(
            @PathVariable String assetId,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endDate) {
        java.time.LocalDate end = endDate != null ? endDate : java.time.LocalDate.now();
        java.time.LocalDate start = startDate != null ? startDate : end.minusDays(6);
        List<AssetPriceHistory> entities = priceHistoryRepository.findByAsset_AssetIdAndTradingDateBetweenOrderByTradingDateAsc(assetId, start, end);
        return entities.stream()
            .map(ph -> new AssetPriceHistoryDTO(
                ph.getAsset().getAssetId(),
                ph.getAsset().getName(),
                ph.getTradingDate(),
                ph.getClosingPrice(),
                ph.getSource()
            ))
            .toList();
    }

    // (I) Get all price history for a specific asset symbol (e.g., IBM)
    @GetMapping("/symbol/{symbol}")
    public List<AssetPriceHistory> getPriceHistoryBySymbol(@PathVariable String symbol) {
        return priceHistoryRepository.findByAsset_NameOrderByTradingDateAsc(symbol);
    }

    /**
     * Import asset price history for a given asset and symbol, only for the last 7 days (including today).
     * This prevents importing excessive data and keeps the database focused on recent prices.
     */
    public ResponseEntity<?> importFromAlphaVantage(
            @PathVariable String assetId,
            @PathVariable String symbol) {
        try {
            LocalDate end = LocalDate.now();
            LocalDate start = end.minusDays(6);
            List<AlphaVantagePriceDTO> alphaObjects = alphaVantageClient.getDailyPriceDTOs(symbol);
            // Filter the DTOs to only include those within the last 7 days
            List<AlphaVantagePriceDTO> filtered = alphaObjects.stream()
                .filter(dto -> !dto.getTradingDate().isBefore(start) && !dto.getTradingDate().isAfter(end))
                .toList();
            assetHistoryService.importPriceHistoryFromAlphaVantage(assetId, filtered, symbol);
            return ResponseEntity.ok("Imported asset price history for last 7 days: " + start + " to " + end);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to import price history: " + ex.getMessage());
        }
    }
}