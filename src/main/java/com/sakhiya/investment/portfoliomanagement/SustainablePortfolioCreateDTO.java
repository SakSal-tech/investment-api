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
}
