package com.sakhiya.investment.riskmanagement.dto;

import java.util.List;

/**
 * helper for building the JSON string (detailsJson) that goes into the Risk entity
 * DTO for capturing all intermediate and final values
 * of a Value-at-Risk (VaR) calculation.
 *
 * - Exposed via API (stored in Risk.detailsJson).
 * - Keeps both raw (possibly negative) and absolute VaR
 *   so downstream consumers can choose which one to use.
 */
public class VaRCalculationDetailsDTO {

    private List<Double> returns;
    private double mean;
    private double stdDev;
    private double zScore;
    private double confidenceLevel;
    private int timeHorizonDays;

    // New fields to capture final results
    private double rawVaR; // signed result from the formula (negative = loss)
    private double absVaR; // magnitude of loss

    public VaRCalculationDetailsDTO(List<Double> returns, double mean, double stdDev,
                                    double zScore, double confidenceLevel, int timeHorizonDays,
                                    double rawVaR) {
        this.returns = returns;
        this.mean = mean;
        this.stdDev = stdDev;
        this.zScore = zScore;
        this.confidenceLevel = confidenceLevel;
        this.timeHorizonDays = timeHorizonDays;
        this.rawVaR = rawVaR;
        this.absVaR = Math.abs(rawVaR); // ensure absolute version is always positive
    }

    // Getters and setters (needed for JSON serialization)

    public List<Double> getReturns() {
        return returns;
    }

    public void setReturns(List<Double> returns) {
        this.returns = returns;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getStdDev() {
        return stdDev;
    }

    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }

    public double getZScore() {
        return zScore;
    }

    public void setZScore(double zScore) {
        this.zScore = zScore;
    }

    public double getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(double confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public int getTimeHorizonDays() {
        return timeHorizonDays;
    }

    public void setTimeHorizonDays(int timeHorizonDays) {
        this.timeHorizonDays = timeHorizonDays;
    }

    public double getRawVaR() {
        return rawVaR;
    }

    public void setRawVaR(double rawVaR) {
        this.rawVaR = rawVaR;
        this.absVaR = Math.abs(rawVaR); // keep in sync
    }

    public double getAbsVaR() {
        return absVaR;
    }

    public void setAbsVaR(double absVaR) {
        this.absVaR = absVaR;
    }
}
