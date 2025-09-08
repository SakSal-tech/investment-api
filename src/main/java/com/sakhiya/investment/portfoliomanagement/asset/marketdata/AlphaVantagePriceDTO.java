package com.sakhiya.investment.portfoliomanagement.asset.marketdata;

/**
 * Simple DTO for price data from AlphaVantageClient. 
 */
public  class AlphaVantagePriceDTO {
    private final java.time.LocalDate tradingDate;
    private final Double closingPrice;

    public AlphaVantagePriceDTO(java.time.LocalDate tradingDate, Double closingPrice) {
                this.tradingDate = tradingDate;
                this.closingPrice = closingPrice;
            }

    public java.time.LocalDate getTradingDate() {
        return tradingDate;
    }

    public Double getClosingPrice() {
        return closingPrice;
    }
}
