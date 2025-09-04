package com.sakhiya.investment.portfoliomanagement.asset;

import org.springframework.stereotype.Service;

import com.sakhiya.investment.portfoliomanagement.asset.marketdata.AlphaVantagePriceDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssetHistoryService {
    private final AssetPriceHistoryRepository priceHistoryRepository;
    private final AssetRepository assetRepository;


        // (I) Use constructor injection for both repositories
        public AssetHistoryService(AssetPriceHistoryRepository priceHistoryRepository, AssetRepository assetRepository) {
            this.priceHistoryRepository = priceHistoryRepository;
            this.assetRepository = assetRepository;
        }

    /**
     * (I) Refactored this method out of RiskService to reduce coupling and follow SRP.
     * Fetches historical daily returns for an asset using its price history.
     *
     * @param assetId The ID of the asset whose returns are to be calculated.
     * @return List of daily returns (as decimals, e.g., 0.01 for 1%) in chronological order.
     */
    public List<Double> getHistoricalReturns(String assetId) {
        List<AssetPriceHistory> priceHistory = priceHistoryRepository.findByAsset_AssetIdOrderByTradingDateAsc(assetId);
        List<Double> returns = new ArrayList<>();
        //If there are fewer than 2 price records, it now logs a warning and returns an empty list, preventing errors in downstream calculations
        // (I) Handle missing or insufficient price data gracefully
        if (priceHistory == null || priceHistory.size() < 2) {
            // Not enough data to calculate returns (need at least 2 prices)
            System.out.println("[WARN] Not enough price history for assetId: " + assetId + ". Returning empty returns list.");
            return returns;
        }
        for (int i = 1; i < priceHistory.size(); i++) {
            double today = priceHistory.get(i).getClosingPrice();
            double yesterday = priceHistory.get(i - 1).getClosingPrice();
            returns.add((today - yesterday) / yesterday);
        }
        return returns;
    }
    
        /**
         * (I) Imports price data from AlphaVantageClient and saves as AssetPriceHistory entities.
         * This method demonstrates how to map API data to your JPA entity and persist it.
         *
         * @param assetId The ID of the asset to associate with the price history
         * @param priceData List of AlphaVantagePrice DTOs containing date and price (from AlphaVantageClient)
         * @param source The source string (e.g., "AlphaVantage")
         */
        public void importPriceHistoryFromAlphaVantage(String assetId, List<AlphaVantagePriceDTO> priceData, String source) {
            // Find the Asset entity by ID
            Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found: " + assetId));
            // For each price record, check for duplicates before saving
            for (AlphaVantagePriceDTO dto : priceData) {
                // Check if a price history already exists for this asset and trading date
                List<AssetPriceHistory> existing = priceHistoryRepository.findByAsset_AssetIdAndTradingDateBetweenOrderByTradingDateAsc(
                    assetId, dto.getTradingDate(), dto.getTradingDate());
                if (existing.isEmpty()) {
                    AssetPriceHistory history = new AssetPriceHistory();
                    history.setAsset(asset);
                    history.setTradingDate(dto.getTradingDate());
                    history.setClosingPrice(dto.getClosingPrice());
                    history.setSource(source);
                    // Save only if not a duplicate
                    priceHistoryRepository.save(history);
                }
                // If a record exists, skip to prevent duplicates
            }
            // This logic ensures only one price history per asset per date is stored.
        }
    

}
