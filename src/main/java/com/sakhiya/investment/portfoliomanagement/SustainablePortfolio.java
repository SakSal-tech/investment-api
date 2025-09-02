package com.sakhiya.investment.portfoliomanagement;

import jakarta.persistence.ElementCollection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.sakhiya.investment.portfoliomanagement.asset.Asset;
import jakarta.persistence.Entity;
@Entity
public class SustainablePortfolio extends Portfolio {

    // JPA requires a public no-arg constructor
    public SustainablePortfolio() {
        super();
    }

    
    @ElementCollection
    private Map<String, Integer> esgScores;      // ESG scores per holding
    @ElementCollection
    private List<String> themeFocus;             // Themes like 'climate', 'human rights'
    @ElementCollection
    private Map<String, String> impactTargets;   // Expected social/environmental impact
    private Integer overallEsgScore;              // Cached overall ESG score
    @ElementCollection
    private List<String> excludedSectors;        // Industries to avoid (e.g., tobacco, fossil fuels)
    @ElementCollection
    private List<String> preferredSectors;       // Preferred sectors for sustainable investment
    private LocalDate lastUpdated;               // For reporting and tracking updates
    private String complianceStatus;             // Compliance with regulations or standards (e.g., UNPRI, SFDR)

    public SustainablePortfolio(String portfolioName, String clientId, LocalDate createdAt,
            LocalDate updatedAt, String investmentGoal, Integer riskLevel, BigDecimal totalValue, List<Asset> assets,
            Map<String, Integer> esgScores, List<String> themeFocus, Map<String, String> impactTargets,
            Integer overallEsgScore, List<String> excludedSectors, List<String> preferredSectors, LocalDate lastUpdated,
            String complianceStatus) {
        super(portfolioName, clientId, createdAt, updatedAt, investmentGoal, riskLevel, totalValue, assets);
        this.esgScores = esgScores;
        this.themeFocus = themeFocus;
        this.impactTargets = impactTargets;
        this.overallEsgScore = overallEsgScore;
        this.excludedSectors = excludedSectors;
        this.preferredSectors = preferredSectors;
        this.lastUpdated = lastUpdated;
        this.complianceStatus = complianceStatus;
    }

    public void setEsgScores(Map<String, Integer> esgScores) {
        this.esgScores = esgScores;
    }

    public void setThemeFocus(List<String> themeFocus) {
        this.themeFocus = themeFocus;
    }

    public void setImpactTargets(Map<String, String> impactTargets) {
        this.impactTargets = impactTargets;
    }

    public void setOverallEsgScore(Integer overallEsgScore) {
        this.overallEsgScore = overallEsgScore;
    }

    public void setExcludedSectors(List<String> excludedSectors) {
        this.excludedSectors = excludedSectors;
    }

    public void setPreferredSectors(List<String> preferredSectors) {
        this.preferredSectors = preferredSectors;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setComplianceStatus(String complianceStatus) {
        this.complianceStatus = complianceStatus;
    }

    public Map<String, Integer> getEsgScores() {
        return esgScores;
    }

    public List<String> getThemeFocus() {
        return themeFocus;
    }

    public Map<String, String> getImpactTargets() {
        return impactTargets;
    }

    public Integer getOverallEsgScore() {
        return overallEsgScore;
    }

    public List<String> getExcludedSectors() {
        return excludedSectors;
    }

    public List<String> getPreferredSectors() {
        return preferredSectors;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public String getComplianceStatus() {
        return complianceStatus;
    }

    
    




}
