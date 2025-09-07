package com.sakhiya.investment.portfoliomanagement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
//Decouples database model from  API contract, entity  have relationships and sensitive fields I don't want to expose. 
//The DTO is a clean, safe representation for clients.Instead of returning the full entity (which could include all related objects), I use toDTO to build a DTO with only the fields I want:



public class SustainablePortfolioDTO {
    private String portfolioId;
    private String portfolioName;
    private String clientName;
    private int assetCount;
    private List<String> assetNames;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String investmentGoal;
    private Integer riskLevel;
    private BigDecimal totalValue;
    private Double overallEsgScore;
    private Map<String, Double> esgScores;
    private Map<String, Double> impactTargets;
    private List<String> themeFocus;
    private List<String> excludedSectors;
    private List<String> preferredSectors;
    private LocalDate lastUpdated;
    private String complianceStatus;

    // Getters and Setters
    public String getPortfolioId() { return portfolioId; }
    public void setPortfolioId(String portfolioId) { this.portfolioId = portfolioId; }

    public String getPortfolioName() { return portfolioName; }
    public void setPortfolioName(String portfolioName) { this.portfolioName = portfolioName; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public int getAssetCount() { return assetCount; }
    public void setAssetCount(int assetCount) { this.assetCount = assetCount; }

    public List<String> getAssetNames() { return assetNames; }
    public void setAssetNames(List<String> assetNames) { this.assetNames = assetNames; }

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

    public Double getOverallEsgScore() { return overallEsgScore; }
    public void setOverallEsgScore(Double overallEsgScore) { this.overallEsgScore = overallEsgScore; }

    public Map<String, Double> getEsgScores() { return esgScores; }
    public void setEsgScores(Map<String, Double> esgScores) { this.esgScores = esgScores; }

    public Map<String, Double> getImpactTargets() { return impactTargets; }
    public void setImpactTargets(Map<String, Double> impactTargets) { this.impactTargets = impactTargets; }

    public List<String> getThemeFocus() { return themeFocus; }
    public void setThemeFocus(List<String> themeFocus) { this.themeFocus = themeFocus; }

    public List<String> getExcludedSectors() { return excludedSectors; }
    public void setExcludedSectors(List<String> excludedSectors) { this.excludedSectors = excludedSectors; }

    public List<String> getPreferredSectors() { return preferredSectors; }
    public void setPreferredSectors(List<String> preferredSectors) { this.preferredSectors = preferredSectors; }

    public LocalDate getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDate lastUpdated) { this.lastUpdated = lastUpdated; }

    public String getComplianceStatus() { return complianceStatus; }
    public void setComplianceStatus(String complianceStatus) { this.complianceStatus = complianceStatus; }
}