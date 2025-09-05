package com.sakhiya.investment.portfoliomanagement.asset;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AssetPriceHistory {
    @Id
    @Column(name = "price_History_Id", columnDefinition = "CHAR(36)")
    private String priceHistoryId = UUID.randomUUID().toString();  // stored UUID as String for easier JPA hypernate 

    @ManyToOne //many price history rows belong to one asset
    @JoinColumn(name="asset_Id", nullable = false) //tells JPA this column in the database (asset_id) is the foreign key.
    // Injecting. back-reference that JPA uses to link the AssetPriceHistory entity back to its parent Asset
    private Asset asset;
    private LocalDate tradingDate;//trading date of the price from the external API AlphaVantage
    private Double closingPrice;
    private String source;//Identify which API the data came from:AlphaVantage

    public AssetPriceHistory() {}// no args constructor. Without it, JPA might throw errors when loading entities from the database

    public AssetPriceHistory(LocalDate tradingDate, Double closingPrice,
            String source) {
        this.tradingDate = tradingDate;
        this.closingPrice = closingPrice;
        this.source = source;
    }
    public LocalDate getTradingDate() {
        return tradingDate;
    }
    public void setTradingDate(LocalDate tradingDate) {
        this.tradingDate = tradingDate;
    }
    public Double getClosingPrice() {
        return closingPrice;
    }
    public void setClosingPrice(Double closingPrice) {
        this.closingPrice = closingPrice;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
        public Asset getAsset() { return asset; }
    public void setAsset(Asset asset) { this.asset = asset; }




}
