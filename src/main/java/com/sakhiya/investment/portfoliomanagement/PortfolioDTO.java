package com.sakhiya.investment.portfoliomanagement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
//response DTOs (used for sending portfolio data back to clients).
// This is created because so much unwanted data was returned by get requests inlcuding all assets, clients and risks associated with each portfolio
//including Historic price data from external API which was something to do with Spring serialization
public class PortfolioDTO {
    private String portfolioId;
    private String portfolioName;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String investmentGoal;
    private Integer riskLevel;
    private BigDecimal totalValue;
    private Double totalVaR;
    private Double totalStressTest;
    private String clientName;
    private List<String> assetNames;
    private List<String> assetIds; 

    private String clientId;


    // getters and setters for above fields

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<String> getAssetIds() {
        return assetIds;
    }

    public void setAssetIds(List<String> assetIds) {
        this.assetIds = assetIds;
    }

    public List<String> getAssetNames() {
        return assetNames;
    }
    public void setAssetNames(List<String> assetNames) {
        this.assetNames = assetNames;
    }
    private int assetCount;

    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public int getAssetCount() {
        return assetCount;
    }
    public void setAssetCount(int assetCount) {
        this.assetCount = assetCount;
    }
    // Getters and setters
    public String getPortfolioId() { return portfolioId; }
    public void setPortfolioId(String portfolioId) { this.portfolioId = portfolioId; }

    public String getPortfolioName() { return portfolioName; }
    public void setPortfolioName(String portfolioName) { this.portfolioName = portfolioName; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }

    public String getInvestmentGoal() { return investmentGoal; }
    public void setInvestmentGoal(String investmentGoal) { this.investmentGoal = investmentGoal; }

    public Integer getRiskLevel() { return riskLevel; }
    public void setRiskLevel(Integer riskLevel) { this.riskLevel = riskLevel; }

    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }

    public Double getTotalVaR() { return totalVaR; }
    public void setTotalVaR(Double totalVaR) { this.totalVaR = totalVaR; }

    public Double getTotalStressTest() { return totalStressTest; }
    public void setTotalStressTest(Double totalStressTest) { this.totalStressTest = totalStressTest; }

}


