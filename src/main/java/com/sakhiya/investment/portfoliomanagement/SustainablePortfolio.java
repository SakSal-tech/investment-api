package com.sakhiya.investment.portfoliomanagement;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.JoinColumn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sakhiya.investment.clientmanagement.Client;
import com.sakhiya.investment.portfoliomanagement.asset.Asset;

// JPA entity representing a sustainable investment portfolio
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class SustainablePortfolio extends Portfolio {

    // Flattened ESG scores for legacy support (single columns)
    @Column(name = "esg_score_env")
    private Double esgScoreEnv;

    @Column(name = "esg_score_social")
    private Double esgScoreSocial;

    @Column(name = "esg_score_gov")
    private Double esgScoreGov;

    // Flattened impact targets for legacy support (single columns)
    @Column(name = "impact_target_carbon")
    private Double impactTargetCarbon;

    @Column(name = "impact_target_water")
    private Double impactTargetWater;

    // Stored as delimited strings for legacy support
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

    @OneToMany
    private List<Asset> assets = new ArrayList<>();

    public SustainablePortfolio() {
        super();
    }

    // --------------------- Getters and Setters ---------------------

    // ESG Scores as a Map (legacy support)
    // This is for backward compatibility with older columns
    public java.util.Map<String, Double> getEsgScoresLegacy() {
        java.util.Map<String, Double> map = new java.util.HashMap<>();
        if (esgScoreEnv != null)
            map.put("environmental", esgScoreEnv);
        if (esgScoreSocial != null)
            map.put("social", esgScoreSocial);
        if (esgScoreGov != null)
            map.put("governance", esgScoreGov);
        return map;
    }

    public void setEsgScoresLegacy(java.util.Map<String, Double> esgScores) {
        this.esgScoreEnv = esgScores != null ? esgScores.getOrDefault("environmental", null) : null;
        this.esgScoreSocial = esgScores != null ? esgScores.getOrDefault("social", null) : null;
        this.esgScoreGov = esgScores != null ? esgScores.getOrDefault("governance", null) : null;
    }

    // Impact Targets as a Map (legacy support)
    // This is for backward compatibility with older columns
    public java.util.Map<String, Double> getImpactTargetsLegacy() {
        java.util.Map<String, Double> map = new java.util.HashMap<>();
        if (impactTargetCarbon != null)
            map.put("carbon", impactTargetCarbon);
        if (impactTargetWater != null)
            map.put("water", impactTargetWater);
        return map;
    }

    public void setImpactTargetsLegacy(java.util.Map<String, Double> impactTargets) {
        this.impactTargetCarbon = impactTargets != null ? impactTargets.getOrDefault("carbon", null) : null;
        this.impactTargetWater = impactTargets != null ? impactTargets.getOrDefault("water", null) : null;
    }

    // --- JPA mappings for collection tables ---
    // These are the actual normalized fields mapped to join tables

    // ESG scores per holding (normalized)
    @ElementCollection
    @CollectionTable(name = "sustainable_portfolio_esg_scores", joinColumns = @JoinColumn(name = "sustainable_portfolio_portfolio_id"))
    @MapKeyColumn(name = "esg_scores_key")
    @Column(name = "esg_scores")
    private Map<String, Double> esgScores; // ESG scores per holding

    // Themes like 'climate', 'human rights' (normalized)
    @ElementCollection
    @CollectionTable(name = "sustainable_portfolio_theme_focus", joinColumns = @JoinColumn(name = "sustainable_portfolio_portfolio_id"))
    @Column(name = "theme_focus")
    private List<String> themeFocus;

    // Expected social/environmental impact (normalized)
    @ElementCollection
    @CollectionTable(name = "sustainable_portfolio_impact_targets", joinColumns = @JoinColumn(name = "sustainable_portfolio_portfolio_id"))
    @MapKeyColumn(name = "impact_targets_key")
    @Column(name = "impact_targets")
    private Map<String, Double> impactTargets;

    // Cached overall ESG score
    private Double overallEsgScore;

    // Industries to avoid (e.g., tobacco, fossil fuels) (database normalised)
    @ElementCollection
    @CollectionTable(name = "sustainable_portfolio_excluded_sectors", joinColumns = @JoinColumn(name = "sustainable_portfolio_portfolio_id"))
    @Column(name = "excluded_sectors")
    private List<String> excludedSectors;

    // Preferred sectors for sustainable investment (database normalised)
    @ElementCollection
    @CollectionTable(name = "sustainable_portfolio_preferred_sectors", joinColumns = @JoinColumn(name = "sustainable_portfolio_portfolio_id"))
    @Column(name = "preferred_sectors")
    private List<String> preferredSectors;

    // For reporting and tracking updates
    private LocalDate lastUpdated;

    // Compliance with regulations or standards (e.g., UNPRI, SFDR)
    private String complianceStatus;

    // Full constructor for all fields
    public SustainablePortfolio(String portfolioName, Client client, LocalDate createdAt,
            LocalDate updatedAt, String investmentGoal, Integer riskLevel, BigDecimal totalValue, List<Asset> assets,
            Map<String, Double> esgScores, List<String> themeFocus, Map<String, Double> impactTargets,
            Double overallEsgScore, List<String> excludedSectors, List<String> preferredSectors, LocalDate lastUpdated,
            String complianceStatus) {
        super(portfolioName, client, createdAt, updatedAt, investmentGoal, riskLevel, totalValue, assets);
        this.esgScores = esgScores;
        this.themeFocus = themeFocus;
        this.impactTargets = impactTargets;
        this.overallEsgScore = overallEsgScore;
        this.excludedSectors = excludedSectors;
        this.preferredSectors = preferredSectors;
        this.lastUpdated = lastUpdated;
        this.complianceStatus = complianceStatus;
    }

    // Theme Focus as List (legacy string field)
    // Used for backward compatibility with delimited string columns
    public java.util.List<String> getThemeFocusLegacy() {
        java.util.List<String> list = new java.util.ArrayList<>();
        if (themeFocusString != null && !themeFocusString.isBlank()) {
            for (String s : themeFocusString.split(","))
                list.add(s.trim());
        }
        return list;
    }

    public void setThemeFocusLegacy(java.util.List<String> list) {
        this.themeFocusString = list != null ? String.join(",", list) : null;
    }

    // Excluded Sectors as List (legacy string field)
    // Used for backward compatibility with delimited string columns
    public java.util.List<String> getExcludedSectorsLegacy() {
        java.util.List<String> list = new java.util.ArrayList<>();
        if (excludedSectorsString != null && !excludedSectorsString.isBlank()) {
            for (String s : excludedSectorsString.split(","))
                list.add(s.trim());
        }
        return list;
    }

    public void setExcludedSectorsLegacy(java.util.List<String> list) {
        this.excludedSectorsString = list != null ? String.join(",", list) : null;
    }

    // Preferred Sectors as List (legacy string field)
    // Used for backward compatibility with delimited string columns
    public java.util.List<String> getPreferredSectorsLegacy() {
        java.util.List<String> list = new java.util.ArrayList<>();
        if (preferredSectorsString != null && !preferredSectorsString.isBlank()) {
            for (String s : preferredSectorsString.split(","))
                list.add(s.trim());
        }
        return list;
    }

    public void setPreferredSectorsLegacy(java.util.List<String> list) {
        this.preferredSectorsString = list != null ? String.join(",", list) : null;
    }

    // lastUpdated
    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    // overallEsgScore
    public Double getOverallEsgScore() {
        return overallEsgScore;
    }

    public void setOverallEsgScore(Double overallEsgScore) {
        this.overallEsgScore = overallEsgScore;
    }

    // complianceStatus
    public String getComplianceStatus() {
        return complianceStatus;
    }

    public void setComplianceStatus(String complianceStatus) {
        this.complianceStatus = complianceStatus;
    }

    // assets
    public java.util.List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(java.util.List<Asset> assets) {
        this.assets = assets;
    }

    // --- Added setters for controller mapping ---
    public void setEsgScoreEnv(Double esgScoreEnv) {
        this.esgScoreEnv = esgScoreEnv;
    }

    public void setEsgScoreSocial(Double esgScoreSocial) {
        this.esgScoreSocial = esgScoreSocial;
    }

    public void setEsgScoreGov(Double esgScoreGov) {
        this.esgScoreGov = esgScoreGov;
    }

    public void setImpactTargetCarbon(Double impactTargetCarbon) {
        this.impactTargetCarbon = impactTargetCarbon;
    }

    public void setImpactTargetWater(Double impactTargetWater) {
        this.impactTargetWater = impactTargetWater;
    }

    public void setThemeFocusString(String themeFocusString) {
        this.themeFocusString = themeFocusString;
    }

    public void setExcludedSectorsString(String excludedSectorsString) {
        this.excludedSectorsString = excludedSectorsString;
    }

    public void setPreferredSectorsString(String preferredSectorsString) {
        this.preferredSectorsString = preferredSectorsString;
    }

    public Map<String, Double> getEsgScores() {
        return esgScores;
    }

    public void setEsgScores(Map<String, Double> esgScores) {
        this.esgScores = esgScores;
    }

    public Map<String, Double> getImpactTargets() {
        return impactTargets;
    }

    public void setImpactTargets(Map<String, Double> impactTargets) {
        this.impactTargets = impactTargets;
    }

    public List<String> getThemeFocus() {
        return themeFocus;
    }

    public void setThemeFocus(List<String> themeFocus) {
        this.themeFocus = themeFocus;
    }

    public List<String> getExcludedSectors() {
        return excludedSectors;
    }

    public void setExcludedSectors(List<String> excludedSectors) {
        this.excludedSectors = excludedSectors;
    }

    public List<String> getPreferredSectors() {
        return preferredSectors;
    }

    public void setPreferredSectors(List<String> preferredSectors) {
        this.preferredSectors = preferredSectors;
    }

}
