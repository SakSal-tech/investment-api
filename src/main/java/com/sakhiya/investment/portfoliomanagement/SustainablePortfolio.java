
package com.sakhiya.investment.portfoliomanagement;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sakhiya.investment.portfoliomanagement.asset.Asset;

// JPA entity representing a sustainable investment portfolio
@Entity
public class SustainablePortfolio extends Portfolio {

    // Primary key for the portfolio
    @Id
    private String portfolioId;

    // Name of the portfolio
    @Column(name = "portfolio_name")
    private String portfolioName;

    // ID of the client who owns this portfolio
    @Column(name = "client_id")
    private String clientId;

    // Portfolio creation timestamp
    @Column(name = "created_at")
    private LocalDate createdAt;

    // Portfolio last updated timestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    // Added lastUpdated field for compatibility with PortfolioService and SustainablePortfolioService
    @Column(name = "last_updated")
    private String lastUpdated;
    // Getter and setter for lastUpdated (added for compatibility)
    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    // ESG (Environmental, Social, Governance) scores stored as JSON in the DB
    @Column(name = "esg_scores")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Double> esgScores = new HashMap<>();

    // Impact targets stored as JSON in the DB
    @Column(name = "impact_targets")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Double> impactTargets = new HashMap<>();

    // Themes the portfolio focuses on, stored as JSON array
    @Column(name = "theme_focus")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> themeFocus = new ArrayList<>();

    // Sectors excluded from investment, stored as JSON array
    @Column(name = "excluded_sectors")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> excludedSectors = new ArrayList<>();

    // Preferred sectors for investment, stored as JSON array
    @Column(name = "preferred_sectors")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> preferredSectors = new ArrayList<>();

    // Investment goal of the portfolio (e.g., growth, income)
    @Column(name = "investment_goal")
    private String investmentGoal;

    // Risk level of the portfolio (e.g., low, medium, high)
    @Column(name = "risk_level")
    private Integer riskLevel;

    // Total value of the portfolio
    @Column(name = "total_value")
    private java.math.BigDecimal totalValue;

    // Overall ESG score for the portfolio
    @Column(name = "overall_esg_score")
    private Double overallEsgScore;

    // Compliance status (e.g., compliant, non-compliant)
    @Column(name = "compliance_status")
    private String complianceStatus;

    // List of assets in the portfolio
    @OneToMany
    private List<Asset> assets = new ArrayList<>();

    // --------------------- Getters and Setters ---------------------

    public String getPortfolioId() { return portfolioId; }
    public void setPortfolioId(String portfolioId) { this.portfolioId = portfolioId; }

    public String getPortfolioName() { return portfolioName; }
    public void setPortfolioName(String portfolioName) { this.portfolioName = portfolioName; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
   
    @Override
    public LocalDate getCreatedAt() { return createdAt; }
    @Override
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    @Override
    public LocalDate getUpdatedAt() { return updatedAt; }
    @Override
    public void setUpdatedAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }

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

    public String getInvestmentGoal() { return investmentGoal; }
    public void setInvestmentGoal(String investmentGoal) { this.investmentGoal = investmentGoal; }

    @Override
    public Integer getRiskLevel() { return riskLevel; }
    @Override
    public void setRiskLevel(Integer riskLevel) { this.riskLevel = riskLevel; }

    @Override
    public java.math.BigDecimal getTotalValue() { return totalValue; }
    @Override
    public void setTotalValue(java.math.BigDecimal totalValue) { this.totalValue = totalValue; }

    public Double getOverallEsgScore() { return overallEsgScore; }
    public void setOverallEsgScore(Double overallEsgScore) { this.overallEsgScore = overallEsgScore; }

    public String getComplianceStatus() { return complianceStatus; }
    public void setComplianceStatus(String complianceStatus) { this.complianceStatus = complianceStatus; }

    public List<Asset> getAssets() { return assets; }
    public void setAssets(List<Asset> assets) { this.assets = assets; }
}
