package com.sakhiya.investment.portfoliomanagement;

import jakarta.persistence.Column;
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

    @Id
    private String portfolioId;

    @Column(name = "portfolio_name")
    private String portfolioName;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "last_updated")
    private String lastUpdated;

    // Flattened ESG scores
    @Column(name = "esg_score_env")
    private Double esgScoreEnv;

    @Column(name = "esg_score_social")
    private Double esgScoreSocial;

    @Column(name = "esg_score_gov")
    private Double esgScoreGov;

    // Flattened impact targets
    @Column(name = "impact_target_carbon")
    private Double impactTargetCarbon;

    @Column(name = "impact_target_water")
    private Double impactTargetWater;

    // Stored as delimited strings
    @Column(name = "theme_focus")
    private String themeFocusString;

    @Column(name = "excluded_sectors")
    private String excludedSectorsString;

    @Column(name = "preferred_sectors")
    private String preferredSectorsString;

    @Column(name = "investment_goal")
    private String investmentGoal;

    @Column(name = "risk_level")
    private Integer riskLevel;

    @Column(name = "total_value")
    private java.math.BigDecimal totalValue;

    @Column(name = "overall_esg_score")
    private Double overallEsgScore;

    @Column(name = "compliance_status")
    private String complianceStatus;

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

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    // In-memory Map for ESG scores
    public Map<String, Double> getEsgScores() {
        Map<String, Double> map = new HashMap<>();
        map.put("ENV", esgScoreEnv);
        map.put("SOC", esgScoreSocial);
        map.put("GOV", esgScoreGov);
        return map;
    }
    public void setEsgScores(Map<String, Double> map) {
        this.esgScoreEnv = map.get("ENV");
        this.esgScoreSocial = map.get("SOC");
        this.esgScoreGov = map.get("GOV");
    }

    // In-memory Map for impact targets
    public Map<String, Double> getImpactTargets() {
        Map<String, Double> map = new HashMap<>();
        map.put("CARBON", impactTargetCarbon);
        map.put("WATER", impactTargetWater);
        return map;
    }
    public void setImpactTargets(Map<String, Double> map) {
        this.impactTargetCarbon = map.get("CARBON");
        this.impactTargetWater = map.get("WATER");
    }

    // In-memory List for themeFocus
    public List<String> getThemeFocus() {
        List<String> list = new ArrayList<>();
        if (themeFocusString != null && !themeFocusString.isBlank()) {
            for (String s : themeFocusString.split(",")) list.add(s.trim());
        }
        return list;
    }
    public void setThemeFocus(List<String> list) {
        this.themeFocusString = String.join(",", list);
    }

    // In-memory List for excludedSectors
    public List<String> getExcludedSectors() {
        List<String> list = new ArrayList<>();
        if (excludedSectorsString != null && !excludedSectorsString.isBlank()) {
            for (String s : excludedSectorsString.split(",")) list.add(s.trim());
        }
        return list;
    }
    public void setExcludedSectors(List<String> list) {
        this.excludedSectorsString = String.join(",", list);
    }

    // In-memory List for preferredSectors
    public List<String> getPreferredSectors() {
        List<String> list = new ArrayList<>();
        if (preferredSectorsString != null && !preferredSectorsString.isBlank()) {
            for (String s : preferredSectorsString.split(",")) list.add(s.trim());
        }
        return list;
    }
    public void setPreferredSectors(List<String> list) {
        this.preferredSectorsString = String.join(",", list);
    }

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
