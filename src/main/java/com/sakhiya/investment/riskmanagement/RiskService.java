package com.sakhiya.investment.riskmanagement;

import com.sakhiya.investment.portfoliomanagement.asset.Asset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class RiskService {

    private final RiskRepository riskRepository;

    public RiskService(RiskRepository riskRepository) {
        this.riskRepository = riskRepository;
    }

    // -------------------- CRUD methods --------------------

    /**
     * Fetch all Risk records from the database.
     * Useful for dashboards or batch processing.
     */
    public List<Risk> getAllRisks() {
        return riskRepository.findAll();
    }

    /**
     * Fetch a specific Risk by its ID.
     * Throws NoSuchElementException if not found.
     */
    public Risk getRiskById(String riskId) {
        return riskRepository.findById(riskId)
                .orElseThrow(() -> new NoSuchElementException("Risk with id " + riskId + " not found"));
    }

    /**
     * Save a new Risk to the database.
     * Validates the object before saving to avoid bad data.
     */
    public Risk createRisk(Risk risk) {
        validateRisk(risk);
        return riskRepository.save(risk);
    }

    /**
     * Update an existing Risk.
     * Only updates fields provided; validates again after changes.
     */
    public Risk updateRisk(String riskId, Risk updatedRisk) {
        validateRisk(updatedRisk);
        Risk currentRisk = getRiskById(riskId);

        // Update all relevant fields
        currentRisk.setType(updatedRisk.getType());
        currentRisk.setCalculationDate(updatedRisk.getCalculationDate());
        currentRisk.setValue(updatedRisk.getValue());
        currentRisk.setConfidenceLevel(updatedRisk.getConfidenceLevel());
        currentRisk.setTimeHorizon(updatedRisk.getTimeHorizon());
        currentRisk.setScenario(updatedRisk.getScenario());
        currentRisk.setCurrency(updatedRisk.getCurrency());
        currentRisk.setAsset(updatedRisk.getAsset());

        // Save updated risk back to DB
        return riskRepository.save(currentRisk);
    }

    /**
     * Delete a Risk by ID.
     * Throws if risk doesn't exist to avoid silent failures.
     */
    public void deleteById(String riskId) {
        if (!riskRepository.existsById(riskId)) {
            throw new NoSuchElementException("Risk with id " + riskId + " not found");
        }
        riskRepository.deleteById(riskId);
    }

    // -------------------- Validation --------------------

    /**
     * Simple checks to make sure all important fields are present
     * and reasonable. Prevents saving invalid risk data.
     */
    public void validateRisk(Risk risk) {
        if (risk == null) throw new IllegalArgumentException("Risk object cannot be null");
        if (risk.getType() == null || risk.getType().isBlank()) throw new IllegalArgumentException("Type cannot be blank");
        if (risk.getCalculationDate() == null) throw new IllegalArgumentException("Calculation date cannot be null");
        if (risk.getValue() == null) throw new IllegalArgumentException("Value cannot be null");
        if (risk.getConfidenceLevel() == null) throw new IllegalArgumentException("Confidence level cannot be null");
        if (risk.getTimeHorizon() == null || risk.getTimeHorizon().isBlank()) {
            throw new IllegalArgumentException("Time horizon must not be blank");
        }
        try {
            int timeHorizon = Integer.parseInt(risk.getTimeHorizon().replaceAll("[^0-9]", ""));
            if (timeHorizon <= 0) {
                throw new IllegalArgumentException("Time horizon must be a positive integer");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Time horizon must be a valid integer", e);
        }
        if (risk.getAsset() == null) throw new IllegalArgumentException("Asset cannot be null");
    }

    // -------------------- VaR Calculation --------------------

    /**
         * Calculate VaR (Value at Risk) for a given asset, persist (save) it as a Risk object in the database.
         * VaR estimates the maximum expected loss at a given confidence level and time horizon.
         * <p>
         * Note: This method saves the calculated Risk entity to the repository and returns the persisted object.
         */
        public Risk calculateVaR(Asset asset, double confidenceLevel, int timeHorizonDays) {
        double varValue = varCalculator(asset, confidenceLevel, timeHorizonDays);

        Risk risk = new Risk();
        risk.setType("VaR");
        risk.setValue(varValue);
        risk.setAsset(asset);
        risk.setCalculationDate(LocalDate.now());
        risk.setConfidenceLevel(confidenceLevel);
        risk.setTimeHorizon(timeHorizonDays + " days");

        return riskRepository.save(risk);
    }


    /**
     * Parametric VaR using the variance-covariance method.
     * Formula: VaR = PortfolioValue * |(mean + Z * StdDev * sqrt(timeHorizon))|
     * VaR is reported as a positive number representing the potential loss (not a signed value).
     */
    public double varCalculator(Asset asset, double confidenceLevel, int timeHorizonDays) {
        // 1. Get historical returns for the asset (daily, weekly, etc.)
        List<Double> returns = getHistoricalReturns(asset);

        // 2. Calculate mean and standard deviation of returns
        double mean = calculateMean(returns); // average return
        double standardDev = calculateStandardDev(returns); // volatility

        // 3. Convert confidence level to Z-score
        double confidenceMultiplier = getZScore(confidenceLevel);

        // 4. Apply the VaR formula (Parametric / Variance-Covariance Method)
        // Use mean in the formula for a more precise calculation
        double var = asset.getValue() * (mean + confidenceMultiplier * standardDev * Math.sqrt(timeHorizonDays));

        // 5. Return VaR amount as a monetary loss
        return var;
    }


    // -------------------- Helper methods --------------------

    /**
     * Dummy method to simulate fetching historical returns.
     * Replace with DB/API call for real application.
     */
    private List<Double> getHistoricalReturns(Asset asset) {
        List<Double> returns = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 30; i++) { // simulate 30 days
            returns.add(random.nextDouble() * 0.02 - 0.01); // -1% to +1% daily
        }
        return returns;
    }

    /**
     * Dummy method to fetch the current value of the asset.
     * Later can use database or API to get real-time prices.
     */
    //private double getCurrentValue(Asset asset) {
        //return asset.getValue();
   // }

    /**
     * Calculate mean (average) of a list of returns.
     */
    private double calculateMean(List<Double> returns) {
        if (returns == null || returns.isEmpty()) return 0;
        double sum = 0;
        for (double r : returns) sum += r;
        return sum / returns.size();
    }

    /**
     * Calculate standard deviation (volatility) of a list of returns.
     */
    private double calculateStandardDev(List<Double> returns) {
        if (returns == null || returns.isEmpty()) return 0;
        if (returns.size() <= 1) return 0;
        double mean = calculateMean(returns);
        double sumSquares = 0;
        for (double r : returns) {
            sumSquares += Math.pow(r - mean, 2);
        }
        return Math.sqrt(sumSquares / (returns.size() - 1));
    }
    /**
     * Convert confidence level (like 95% or 99%) to Z-score for VaR calculation.
     * 
     * Note: Only supports 0.95 and 0.99 confidence levels.
     * Returns negative Z-scores, which is standard for VaR (representing loss quantiles).
     */
    private double getZScore(double confidenceLevel) {
        if (confidenceLevel == 0.95) return -1.65;
        if (confidenceLevel == 0.99) return -2.33;
        throw new IllegalArgumentException("Unsupported confidence level: " + confidenceLevel);
    }


    public List<Risk> getByType(String type) {
    return riskRepository.findByType(type);
}

public List<Risk> getByAssetId(String assetId) {
    return riskRepository.findByAsset_AssetId(assetId);
}

public List<Risk> getByCalculationDate(LocalDate date) {
    return riskRepository.findByCalculationDate(date);
}

public List<Risk> getByValueGreaterThanEqual(Double value) {
    return riskRepository.findByValueGreaterThanEqual(value);
}

public List<Risk> getByConfidenceLevel(Double confidenceLevel) {
    return riskRepository.findByConfidenceLevel(confidenceLevel);
}

public List<Risk> getByScenario(String scenario) {
    return riskRepository.findByScenario(scenario);
}

public List<Risk> getByCurrency(String currency) {
    return riskRepository.findByCurrency(currency);
}



}
