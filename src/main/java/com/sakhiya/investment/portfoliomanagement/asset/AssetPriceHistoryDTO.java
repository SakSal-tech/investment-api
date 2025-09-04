package com.sakhiya.investment.portfoliomanagement.asset;

import java.time.LocalDate;
/**
 * DTO for returning clean price history data from the API endpoint /api/asset-price-history/asset/{assetId}.
 * 
 * Reason for creation:
 * - The default entity serialization in Spring Boot returns deeply nested and repeated data (asset, portfolio, client, etc.)
 *   when I query price history, causing large and confusing output in Postman.
 * - This DTO solves the problem by including only the essential fields needed for historic price queries:
 *   trading date, closing price, source, asset ID, and asset name.
 * - Using this DTO in my controller will make the API response much simpler and focused, avoiding unnecessary details.
 */
public class AssetPriceHistoryDTO {

    // I am only asking the client to provide assetId, not the full Asset object.
    private String assetId;
    private String assetName;
    private LocalDate tradingDate; // Date of the price
    private Double closingPrice;   // Closing price from the API
    private String source;         // Identify which API the data came from

    // No-args constructor is needed for Spring Boot to map JSON to this object
    public AssetPriceHistoryDTO() {}

    // All-args constructor can be useful for testing or mapping
    public AssetPriceHistoryDTO(String assetId, String assetName, LocalDate tradingDate, Double closingPrice, String source) {
        this.assetId = assetId;
        this.assetName = assetName;
        this.tradingDate = tradingDate;
        this.closingPrice = closingPrice;
        this.source = source;
    }

    // Getters and setters are required for Spring Boot JSON binding
    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }

    public String getAssetName() { return assetName; }
    public void setAssetName(String assetName) { this.assetName = assetName; }

    public LocalDate getTradingDate() { return tradingDate; }
    public void setTradingDate(LocalDate tradingDate) { this.tradingDate = tradingDate; }

    public Double getClosingPrice() { return closingPrice; }
    public void setClosingPrice(Double closingPrice) { this.closingPrice = closingPrice; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
