package com.sakhiya.investment.riskmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sakhiya.investment.portfoliomanagement.asset.Asset;
import com.sakhiya.investment.portfoliomanagement.asset.AssetPriceHistory;
import com.sakhiya.investment.portfoliomanagement.asset.AssetHistoryService;
import com.sakhiya.investment.riskmanagement.dto.VaRCalculationDetailsDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiskService {

    private final RiskRepository riskRepository;
    // Refactored: Inject AssetHistoryService instead of directly using AssetPriceHistoryRepository
    @Autowired
    private AssetHistoryService assetHistoryService;

    @Autowired
    private com.sakhiya.investment.portfoliomanagement.asset.AssetPriceHistoryRepository priceHistoryRepository;

    public RiskService(RiskRepository riskRepository, AssetHistoryService assetHistoryService) {
        this.riskRepository = riskRepository;
        this.assetHistoryService = assetHistoryService;
    }

    // Fetch all Risk records from the database.
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
        if (risk == null)
            throw new IllegalArgumentException("Risk object cannot be null");
        if (risk.getType() == null || risk.getType().isBlank())
            throw new IllegalArgumentException("Type cannot be blank");
        if (risk.getCalculationDate() == null)
            throw new IllegalArgumentException("Calculation date cannot be null");
        if (risk.getValue() == null)
            throw new IllegalArgumentException("Value cannot be null");
        if (risk.getConfidenceLevel() == null)
            throw new IllegalArgumentException("Confidence level cannot be null");
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
        if (risk.getAsset() == null)
            throw new IllegalArgumentException("Asset cannot be null");
    }

    // -------------------- VaR Calculation --------------------

    /**
     * Calculate VaR (Value at Risk) for a given asset, persist (save) it as a Risk
     * object in the database.
     * VaR estimates the maximum expected loss at a given confidence level and time
     * horizon.
     * This method saves the calculated Risk entity to the repository and returns
     * the persisted object.
     */
    /**
     * Calculate VaR (Value at Risk) for a given asset, persist (save) it as a Risk
     * object in the database.
     * This version uses assetId to fetch historical prices for risk calculation.
     *
     * @param assetId The ID of the asset
     * @param confidenceLevel The confidence level for VaR (e.g., 0.95)
     * @param timeHorizonDays The time horizon in days
     * @return The persisted Risk object
     */
    public Risk createAndSaveVaR(String assetId, double confidenceLevel, int timeHorizonDays) {
        // Fetch the Asset object by ID (needed for value and to link to Risk)
        // Refactored: Fetch asset from price history repository for linking to Risk
        Asset asset = priceHistoryRepository.findByAsset_AssetIdOrderByTradingDateAsc(assetId)
                .stream().findFirst()
                .map(AssetPriceHistory::getAsset)
                .orElseThrow(() -> new NoSuchElementException("Asset with id " + assetId + " not found in price history"));

        // Fetch historical returns from AssetHistoryService
        List<Double> returns = assetHistoryService.getHistoricalReturns(assetId);

        // Calculate mean (average daily return) and standard deviation
        double mean = calculateMean(returns); // average return
        double stdDev = calculateStandardDev(returns); // Calculate volatility of returns

        // Z-score for the given confidence level
        double zScore = getZScore(confidenceLevel);

        // Calculate raw VaR (negative for losses) 
        double rawVaR = asset.getValue() * (mean + zScore * stdDev * Math.sqrt(timeHorizonDays));

        // Build DTO for detailsJson 
        VaRCalculationDetailsDTO details = new VaRCalculationDetailsDTO(
                returns, mean, stdDev, zScore, confidenceLevel, timeHorizonDays, rawVaR
        );

        // Serialize DTO to JSON. Uses ObjectMapper to serialize that DTO into a JSON string.
        ObjectMapper mapper = new ObjectMapper();
        String detailsJson;
        try {
            detailsJson = mapper.writeValueAsString(details);
        } catch (Exception e) {
            detailsJson = "{}"; // fallback
        }

        // Build Risk entity
        Risk risk = new Risk();
        risk.setType("VaR");
        risk.setValue(rawVaR); //Keep raw negative value
        risk.setAsset(asset);
        risk.setCalculationDate(LocalDate.now());
        risk.setConfidenceLevel(confidenceLevel);
        risk.setTimeHorizon(timeHorizonDays + " days");
        risk.setDetailsJson(detailsJson); // Save detailed JSON

        return riskRepository.save(risk);
    }

    /**
     * Parametric VaR using the variance-covariance method, using assetId and value.
     * This version fetches historical returns from the DB using assetId.
     *
     * @param assetId The ID of the asset
     * @param assetValue The current value of the asset
     * @param confidenceLevel The confidence level for VaR (e.g., 0.95)
     * @param timeHorizonDays The time horizon in days
     * @return The calculated VaR value (negative = loss)
     */
    public double varCalculator(String assetId, double assetValue, double confidenceLevel, int timeHorizonDays) {
        // Get historical returns for the asset from DB
        List<Double> returns = assetHistoryService.getHistoricalReturns(assetId);

        // Calculate mean and standard deviation
        double mean = calculateMean(returns); // average daily return
        double stdDev = calculateStandardDev(returns); // volatility

        // Z-score
        double zScore = getZScore(confidenceLevel);

        // Apply the VaR formula (signed, negative for loss) 
        double var = assetValue * (mean + zScore * stdDev * Math.sqrt(timeHorizonDays));

        return var; //Return signed VaR
    }

    // -------------------- Helper methods --------------------

    private double calculateMean(List<Double> returns) {
        if (returns == null || returns.isEmpty())
            return 0;
        double sum = 0;
        for (double r : returns)
            sum += r; // Add each daily return to the sum.
        return sum / returns.size();// Divide the total by the number of returns average daily return (mean).
    }

    private double calculateStandardDev(List<Double> returns) {
        if (returns == null || returns.isEmpty())// No returns. volatility = 0.
            return 0;
        if (returns.size() <= 1)// Only 1 return therefore cannot calculate deviation
            return 0;
        double mean = calculateMean(returns);
        double sumSquares = 0;
        for (double r : returns) {
            sumSquares += Math.pow(r - mean, 2);// For each return:(r - mean) = How far is the return from the average?
        }
        return Math.sqrt(sumSquares / (returns.size() - 1));// Take square root standard deviation (volatility in %).
    }

    private double getZScore(double confidenceLevel) {
        // Negative because in VaR I am looking at the loss tail of the distribution
        // (worst-case scenario).
        if (confidenceLevel == 0.90)
            return -1.2816; // 90% one-tailed
        if (confidenceLevel == 0.95)// 95% confidence →=-1.65 standard deviations below the mean
            return -1.65;
        if (confidenceLevel == 0.99)// 99% confidence = -2.33 standard deviations below the mean.
            return -2.33;
        throw new IllegalArgumentException("Unsupported confidence level: " + confidenceLevel);
    }

    /**
     * Uses probability & historical volatility such as Historical events. Analysts
     * look at past crises:
     * 2008 financial crash: many assets dropped ~30-40% in days. 2020 COVID shock:
     * S&P 500 fell ~35% in a month.
     * Stress Tests ask:“What if a really extreme scenario happens, regardless of
     * probability?”
     * Calculate and persist a Stress Test risk for a given asset and scenario.
     * - Applies a scenario-based shock to the asset value (e.g., market crash,
     * interest rate shock).
     * - Persists the result as a Risk entity of type "StressTest" for reporting and
     * analytics.
     *
     * @param asset    The asset to stress test (Asset object, must not be null)
     * @param scenario The scenario name (e.g., "Market Crash", "Interest Rate
     *                 Shock")
     * @return The persisted Risk object representing the stress test result
     */
    public Risk stressTestCalculator(Asset asset, String scenario) {
        double shockFactor;
        // using switch case instead of if statement for better efficiency.
        switch (scenario) {
            case "Market Crash":
                shockFactor = -0.3; // -30% drop. Asset price falls 30%
                break;
            case "Interest Rate Shock":
                shockFactor = -0.1; // -10% drop
                break;
            case "Commodity Spike":
                shockFactor = 0.2; // +20% increase
                break;
            default:
                shockFactor = -0.15; // Default: -15% drop
        }

        // Take the current value of the asset.Multiply by (1 + shockFactor).
        // If shockFactor = -0.3 → 70% of the value remainsExample: Asset = $1000,
        // Market Crash (−30%) → $1000 × (0.7) = $700 stressed value.
        double stressedValue = asset.getValue() * (1 + shockFactor);
        Risk risk = new Risk();// instantiating a new Risk entity that represents this stress test result.
        risk.setType("StressTest");// Label this risk as a Stress Test or "VaR" " types
        risk.setScenario(scenario);// Saves the exact scenario used, e.g. "Market Crash", "Interest Rate Shock"
        risk.setValue(stressedValue);// Records the post-shock value of the asset. Shock factor = “what-if
                                     // assumption” (decided by humans/regulators). Post-shock value = “asset’s
                                     // simulated worth under that assumption.” 
        risk.setAsset(asset);// Associates this risk record with the specific asset that was tested. A
                             // portfolio can have multiple assets
        risk.setCalculationDate(LocalDate.now());// Saves today’s date to know when this stress test was performed
        risk.setDescription("Stress test: " + scenario + " (" + (shockFactor * 100) + "% shock)");// Creates a
                                                                                                  // human-readable
                                                                                                  // description from
                                                                                                  // the database
                                                                                                  // report. shockFactor
                                                                                                  // is stored as a
                                                                                                  // decimal fraction
                                                                                                  // multiply it by 100
                                                                                                  // to convert as
                                                                                                  // percent

        // Check the below. This game me errors in the past as JPA (Hibernate) doesn’t
        // natively support JSON fields unless your DB column is set to a JSON type
        // (like in PostgreSQL with jsonb).
        // AS I am using H2, MySQL, or plain VARCHAR columns, this JSON will just be
        // stored as a string.
        risk.setDetailsJson("{\"shockFactor\":" + shockFactor + "}");

        return riskRepository.save(risk);// Save to the database
    }

}
