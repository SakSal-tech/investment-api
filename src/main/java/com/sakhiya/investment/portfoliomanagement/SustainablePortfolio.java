package com.sakhiya.investment.portfoliomanagement;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.sakhiya.investment.portfoliomanagement.asset.Asset;

// JPA entity representing a sustainable investment portfolio
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
@Entity
public class SustainablePortfolio extends Portfolio {



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

    @OneToMany
    private List<Asset> assets = new ArrayList<>();


    // --------------------- Getters and Setters ---------------------

    // ESG Scores as a Map
    public java.util.Map<String, Double> getEsgScores() {
        java.util.Map<String, Double> map = new java.util.HashMap<>();
        if (esgScoreEnv != null) map.put("environmental", esgScoreEnv);
        if (esgScoreSocial != null) map.put("social", esgScoreSocial);
        if (esgScoreGov != null) map.put("governance", esgScoreGov);
        return map;
    }
    public void setEsgScores(java.util.Map<String, Double> esgScores) {
        this.esgScoreEnv = esgScores != null ? esgScores.getOrDefault("environmental", null) : null;
        this.esgScoreSocial = esgScores != null ? esgScores.getOrDefault("social", null) : null;
        this.esgScoreGov = esgScores != null ? esgScores.getOrDefault("governance", null) : null;
    }

    // Impact Targets as a Map
    public java.util.Map<String, Double> getImpactTargets() {
        java.util.Map<String, Double> map = new java.util.HashMap<>();
        if (impactTargetCarbon != null) map.put("carbon", impactTargetCarbon);
        if (impactTargetWater != null) map.put("water", impactTargetWater);
        return map;
    }
    public void setImpactTargets(java.util.Map<String, Double> impactTargets) {
        this.impactTargetCarbon = impactTargets != null ? impactTargets.getOrDefault("carbon", null) : null;
        this.impactTargetWater = impactTargets != null ? impactTargets.getOrDefault("water", null) : null;
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
        //super(portfolioName, clientId, createdAt, updatedAt, investmentGoal, riskLevel, totalValue, assets);
        super(portfolioName,cl)
        this.esgScores = esgScores;
        this.themeFocus = themeFocus;
        this.impactTargets = impactTargets;
        this.overallEsgScore = overallEsgScore;
        this.excludedSectors = excludedSectors;
        this.preferredSectors = preferredSectors;
        this.lastUpdated = lastUpdated;
        this.complianceStatus = complianceStatus;
    }

    // Theme Focus as List
    public java.util.List<String> getThemeFocus() {
        java.util.List<String> list = new java.util.ArrayList<>();
        if (themeFocusString != null && !themeFocusString.isBlank()) {
            for (String s : themeFocusString.split(",")) list.add(s.trim());
        }
        return list;
    }
    public void setThemeFocus(java.util.List<String> list) {
        this.themeFocusString = list != null ? String.join(",", list) : null;
    }

    // Excluded Sectors as List
    public java.util.List<String> getExcludedSectors() {
        java.util.List<String> list = new java.util.ArrayList<>();
        if (excludedSectorsString != null && !excludedSectorsString.isBlank()) {
            for (String s : excludedSectorsString.split(",")) list.add(s.trim());
        }
        return list;
    }
    public void setExcludedSectors(java.util.List<String> list) {
        this.excludedSectorsString = list != null ? String.join(",", list) : null;
    }

    // Preferred Sectors as List
    public java.util.List<String> getPreferredSectors() {
        java.util.List<String> list = new java.util.ArrayList<>();
        if (preferredSectorsString != null && !preferredSectorsString.isBlank()) {
            for (String s : preferredSectorsString.split(",")) list.add(s.trim());
        }
        return list;
    }
    public void setPreferredSectors(java.util.List<String> list) {
        this.preferredSectorsString = list != null ? String.join(",", list) : null;
    }

    // lastUpdated
    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    // overallEsgScore
    public Double getOverallEsgScore() { return overallEsgScore; }
    public void setOverallEsgScore(Double overallEsgScore) { this.overallEsgScore = overallEsgScore; }

    // complianceStatus
    public String getComplianceStatus() { return complianceStatus; }
    public void setComplianceStatus(String complianceStatus) { this.complianceStatus = complianceStatus; }

    // assets
    public java.util.List<Asset> getAssets() { return assets; }
    public void setAssets(java.util.List<Asset> assets) { this.assets = assets; }

    // --- Added setters for controller mapping ---
    public void setEsgScoreEnv(Double esgScoreEnv) { this.esgScoreEnv = esgScoreEnv; }
    public void setEsgScoreSocial(Double esgScoreSocial) { this.esgScoreSocial = esgScoreSocial; }
    public void setEsgScoreGov(Double esgScoreGov) { this.esgScoreGov = esgScoreGov; }
    public void setImpactTargetCarbon(Double impactTargetCarbon) { this.impactTargetCarbon = impactTargetCarbon; }
    public void setImpactTargetWater(Double impactTargetWater) { this.impactTargetWater = impactTargetWater; }
    public void setThemeFocusString(String themeFocusString) { this.themeFocusString = themeFocusString; }
    public void setExcludedSectorsString(String excludedSectorsString) { this.excludedSectorsString = excludedSectorsString; }
    public void setPreferredSectorsString(String preferredSectorsString) { this.preferredSectorsString = preferredSectorsString; }

}
