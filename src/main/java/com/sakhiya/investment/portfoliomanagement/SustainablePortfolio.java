package com.sakhiya.investment.portfoliomanagement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.sakhiya.investment.portfoliomanagement.asset.Asset;

public class SustainablePortfolio extends Portfolio {

    
    private Map<String, Integer> esgScores;      // ESG scores per holding
    private List<String> themeFocus;             // Themes like 'climate', 'human rights'
    private Map<String, String> impactTargets;   // Expected social/environmental impact
    private double overallEsgScore;              // Cached overall ESG score
    private List<String> excludedSectors;        // Industries to avoid (e.g., tobacco, fossil fuels)
    private List<String> preferredSectors;       // Preferred sectors for sustainable investment
    private LocalDate lastUpdated;               // For reporting and tracking updates
    private String complianceStatus;             // Compliance with regulations or standards (e.g., UNPRI, SFDR)

    public SustainablePortfolio(String portfolioName, UUID clientId, LocalDate createdAt,
            LocalDate updatedAt, String investmentGoal, Double riskLevel, BigDecimal totalValue, List<Asset> assets,
            Map<String, Integer> esgScores, List<String> themeFocus, Map<String, String> impactTargets,
            double overallEsgScore, List<String> excludedSectors, List<String> preferredSectors, LocalDate lastUpdated,
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

    public void setOverallEsgScore(double overallEsgScore) {
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

    public double getOverallEsgScore() {
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
