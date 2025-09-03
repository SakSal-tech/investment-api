package com.sakhiya.investment.riskmanagement.dto;

/**
 * DTO for summarizing risk calculations for a specific asset.
 * I did this because aggregate values like totalVaR and totalStressTest
 * are not properties of a single Risk entity, but are computed from multiple
 * Risk records. Using a DTO keeps the API response clean and avoids
 * polluting the entity model.
 */
public class RiskSummaryDTO {
    private double totalVaR;
    private double totalStressTest;

    public RiskSummaryDTO() {}

    public RiskSummaryDTO(double totalVaR, double totalStressTest) {
        this.totalVaR = totalVaR;
        this.totalStressTest = totalStressTest;
    }

    public double getTotalVaR() {
        return totalVaR;
    }

    public void setTotalVaR(double totalVaR) {
        this.totalVaR = totalVaR;
    }

    public double getTotalStressTest() {
        return totalStressTest;
    }

    public void setTotalStressTest(double totalStressTest) {
        this.totalStressTest = totalStressTest;
    }
}
