package com.sakhiya.investment.portfoliomanagement;

import java.math.BigDecimal;

public class SustainablePortfolioCreateDTO {
    public String portfolioName;
    public String clientId;
    public String investmentGoal;
    public Integer riskLevel;
    public BigDecimal totalValue;
    public String createdAt;
    public String updatedAt;
    public String lastUpdated;
    public Double overallEsgScore;
    public String complianceStatus;
    public Double esgScoreEnv;
    public Double esgScoreSocial;
    public Double esgScoreGov;
    public Double impactTargetCarbon;
    public Double impactTargetWater;
    public String themeFocusString;
    public String excludedSectorsString;
    public String preferredSectorsString;

    // --- Added for @ElementCollection fields ---
    // These fields allow direct population of the JPA collection tables
    // I changed this to Map<String, Double> to match the entity and avoid conversion logic.
    public java.util.Map<String, Double> esgScores; // For esgScores map
    public java.util.List<String> themeFocus;        // For themeFocus list
    // I changed these to Map<String, Double> to match the entity fields in SustainablePortfolio.
    // This avoids the need for conversion logic in the controller and ensures that JPA @ElementCollection
    // fields are populated correctly. Using Double allows for both integer and decimal values, which is
    // important for ESG scores and impact targets that may not always be whole numbers.
    public java.util.Map<String, Double> impactTargets; // For impactTargets map
    public java.util.List<String> excludedSectors;   // For excludedSectors list
    public java.util.List<String> preferredSectors;  // For preferredSectors list
}
