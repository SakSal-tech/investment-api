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

    //Fetch all Risk records from the database.
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
     * Calculate VaR (Value at Risk) for a given asset, persist (save) it as a Risk object in the database.
     * VaR estimates the maximum expected loss at a given confidence level and time  horizon.
     * This method saves the calculated Risk entity to the repository and returns the persisted object.
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
     * StdDev Standard Deviation = volatility (how much it fluctuates).
     * Mean = average return (are you making money on average?).
     * Z-Score (Confidence Level)
     * Formula: VaR = PortfolioValue * |(mean + Z * StdDev * sqrt(timeHorizon))|
     * VaR is reported as a positive number representing the potential loss (not a
     * signed value).
     */
    public double varCalculator(Asset asset, double confidenceLevel, int timeHorizonDays) {
        // Get historical returns for the asset (daily, weekly, etc.)
        List<Double> returns = getHistoricalReturns(asset);

        // Calculate mean (average daily return) and standard deviation of returns.  
        double mean = calculateMean(returns); // average return
        double standardDev = calculateStandardDev(returns); // Calculate volatility of returns

        // Convert confidence level to Z-score. Converts confidence level (95% → -1.65, 99% → -2.33) to a multiplier.
        double confidenceMultiplier = getZScore(confidenceLevel);

        //  Apply the VaR formula (Parametric / Variance-Covariance Method)
        //Multiply by asset.getValue() → convert % risk into actual money.Multiply by Math.sqrt(timeHorizonDays) = scale daily volatility to the chosen time horizon
        //Add mean → corrects for typical expected return
        // Using mean in the formula for a more precise calculation. Tells the worst-case loss asset might face with X% confidence over Y days?
        //use the square-root-of-time rule (works if returns are independent day-to-day).Time horizon → scales risk from 1 day to multiple days using T squareroot
        double var = asset.getValue() * (mean + confidenceMultiplier * standardDev * Math.sqrt(timeHorizonDays));

        // Return Value at Risk estimated worst-case monetary loss for that time period 
        return var;
    }

    // -------------------- Helper methods --------------------

    /**
     * Dummy method to simulate fetching historical returns.
     * Replace with DB/API call for real application.
     */
    private List<Double> getHistoricalReturns(Asset asset) {
        //list to store daily returns for the asset
        List<Double> returns = new ArrayList<>();
        //random number generator to simulate daily returns. Remember to change this fetch historical prices of the asset from the database
        Random random = new Random();
        for (int i = 0; i < 30; i++) { // Loop 30 times to simulate 30 days of returns
            //random.nextDouble +++++++++++++Generates a random decimal between 0.0 (inclusive) and 1.0 (exclusive).
            returns.add(random.nextDouble() * 0.02 - 0.01); // For each day:* 0.02 - 0.01 transforms it into a return between -1% and +1%
        }
        return returns;//Return the list of simulated daily returns
    }
    //when database is populated  changing the above method
    /*private List<Double> getHistoricalReturns(Asset asset) {
    // fetch actual daily prices from DB
    List<Double> prices = priceRepository.findPricesByAsset(asset.getAssetId());
    List<Double> returns = new ArrayList<>();
    for (int i = 1; i < prices.size(); i++) {
        //prices.get(i) = toda's closing price. yesterday's closing price prices.get(i-1). Take away yesterday's 
        //returns.add(...) → add this daily return to the list of all daily returns
        returns.add((prices.get(i) - prices.get(i-1)) / prices.get(i-1));
    }
    return returns;
}
 */

    /**
     * Dummy method to fetch the current value of the asset.
     * Later can use database or API to get real-time prices.
     */
    // private double getCurrentValue(Asset asset) {
    // return asset.getValue();
    // }

    /**
     * Calculate mean (average) of a list of returns.
     */
    private double calculateMean(List<Double> returns) {
        if (returns == null || returns.isEmpty())
            return 0;
        double sum = 0;
        for (double r : returns)
            sum += r; //Add each daily return to the sum.
        return sum / returns.size();//Divide the total by the number of returns average daily return (mean).
    }

    /**
     * Calculate standard deviation (volatility) of a list of returns.
     * Standard deviation measures how much the asset's daily returns swing around the mean:
     * Small std dev means stable asset (less risky)
    Large std devmeans  volatile asset (more risky)
     */
    private double calculateStandardDev(List<Double> returns) {
        if (returns == null || returns.isEmpty())//No returns. volatility = 0.
            return 0;
        if (returns.size() <= 1)//Only 1 return therefore cannot calculate deviation
            return 0;
        double mean = calculateMean(returns);
        double sumSquares = 0;
        for (double r : returns) {
            //pow squares each deviation power of 2. Square it so that negatives don't cancel positives e.g.  +0.05 and -0.05 would average to zero, which hides risk
            sumSquares += Math.pow(r - mean, 2);// For each return:(r - mean) = How far is the return from the average?
        }
        //find variance which is the average of the squared deviations from the mean. Variance shows how volatile an asset
        //Square Root to get Standard Deviation
        return Math.sqrt(sumSquares / (returns.size() - 1));//Take square root standard deviation (volatility in %).
    }

    /**
     * Convert confidence level (like 95% or 99%) to Z-score for VaR calculation.
     * 
     * Note: Only supports 0.95 and 0.99 confidence levels.
     * Returns negative Z-scores, which is standard for VaR (representing loss
     * quantiles).
     */
    private double getZScore(double confidenceLevel) {
        //Negative because in VaR I am looking at the loss tail of the distribution (worst-case scenario).
        if (confidenceLevel == 0.90) 
        return -1.2816; // 90% one-tailed
        if (confidenceLevel == 0.95)//95% confidence →=-1.65 standard deviations below the mean
            return -1.65;
        if (confidenceLevel == 0.99)//99% confidence = -2.33 standard deviations below the mean.
            return -2.33;
        throw new IllegalArgumentException("Unsupported confidence level: " + confidenceLevel);
    }

    /**
     * Uses probability & historical volatilitysuch as Historical events. Analysts look at past crises:
     * 2008 financial crash: many assets dropped ~30-40% in days. 2020 COVID shock: S&P 500 fell ~35% in a month.
     * Stress Tests ask:“What if a really extreme scenario happens, regardless of probability?”
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
        // using switch case instead of if statement for better efficency.
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

        //Take the current value of the asset.Multiply by (1 + shockFactor).
        // If shockFactor = -0.3 → 70% of the value remainsExample: Asset = $1000, Market Crash (−30%) → $1000 × (0.7) = $700 stressed value.
        double stressedValue = asset.getValue() * (1 + shockFactor);
        Risk risk = new Risk();//instantiating a new Risk entity that represents this stress test result.
        risk.setType("StressTest");//Label this risk as a Stress Test or "VaR" " types
        risk.setScenario(scenario);//Saves the exact scenario used, e.g. "Market Crash", "Interest Rate Shock"
        risk.setValue(stressedValue);//Records the post-shock value of the asset. Shock factor = “what-if assumption” (decided by humans/regulators). Post-shock value = “asset’s simulated worth under that assumption.”
        risk.setAsset(asset);//Associates this risk record with the specific asset that was tested. A portfolio can have multiple assets
        risk.setCalculationDate(LocalDate.now());//Saves today’s date to know when this stress test was performed
        risk.setDescription("Stress test: " + scenario + " (" + (shockFactor * 100) + "% shock)");//Creates a human-readable description from the database report. shockFactor is stored as a decimal fraction multiply it by 100 to convert as percent
        
        // Check the below. This game me errors in the past as JPA (Hibernate) doesn’t natively support JSON fields unless your DB column is set to a JSON type (like in PostgreSQL with jsonb). 
        //AS I am using H2, MySQL, or plain VARCHAR columns, this JSON will just be stored as a string.
        risk.setDetailsJson("{\"shockFactor\":" + shockFactor + "}");

        return riskRepository.save(risk);//Save to the database
    }

}
