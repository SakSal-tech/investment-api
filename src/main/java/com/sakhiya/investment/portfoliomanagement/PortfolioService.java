package com.sakhiya.investment.portfoliomanagement;


import com.sakhiya.investment.portfoliomanagement.asset.Asset;
import com.sakhiya.investment.riskmanagement.Risk;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class to handle operations related to Portfolio,
 * including calculation and storage of aggregate risk totals.
 */
@Service
public class PortfolioService {

    // Repository to persist portfolio updates
    private final PortfolioRepository portfolioRepository;

    // Constructor injection for the repository
    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    /**
     * Recalculate and store total VaR and StressTest values for a portfolio.
     * This method loops through all assets and their risks and updates the portfolio totals.
     *
     * @param portfolio the portfolio to update
     */
    @Transactional
    public void updatePortfolioRiskTotals(Portfolio portfolio) {
        if (portfolio == null) return; // safety check

        double totalVaR = 0.0;        // accumulator for VaR
        double totalStress = 0.0;     // accumulator for StressTest

        // Loop through all assets in the portfolio
        if (portfolio.getAssets() != null) {
            for (Asset asset : portfolio.getAssets()) {
                if (asset.getRisks() != null) {
                    // Loop through all risks of the asset
                    for (Risk risk : asset.getRisks()) {
                        if (risk.getValue() != null) {
                            // Sum VaR risks
                            if ("VaR".equalsIgnoreCase(risk.getType())) {
                                totalVaR += risk.getValue();
                            }
                            // Sum StressTest risks
                            else if ("StressTest".equalsIgnoreCase(risk.getType())) {
                                totalStress += risk.getValue();
                            }
                        }
                    }
                }
            }
        }

        // Store totals in the portfolio entity
        portfolio.setTotalVaR(totalVaR);
        portfolio.setTotalStressTest(totalStress);

        // Persist the updated portfolio
        portfolioRepository.save(portfolio);
    }

    /**
     * Calculate total VaR dynamically without storing in the database.
     *
     * @param portfolio the portfolio to calculate
     * @return total VaR value
     */
    public double calculateTotalVaR(Portfolio portfolio) {
        if (portfolio == null || portfolio.getAssets() == null) return 0.0;

        double totalVaR = 0.0;

        for (Asset asset : portfolio.getAssets()) {
            if (asset.getRisks() == null) continue;
            for (Risk risk : asset.getRisks()) {
                if ("VaR".equalsIgnoreCase(risk.getType()) && risk.getValue() != null) {
                    totalVaR += risk.getValue();
                }
            }
        }

        return totalVaR;
    }

    /**
     * Calculate total StressTest dynamically without storing in the database.
     *
     * @param portfolio the portfolio to calculate
     * @return total StressTest value
     */
    public double calculateTotalStressTest(Portfolio portfolio) {
        if (portfolio == null || portfolio.getAssets() == null) return 0.0;

        double totalStress = 0.0;

        for (Asset asset : portfolio.getAssets()) {
            if (asset.getRisks() == null) continue;
            for (Risk risk : asset.getRisks()) {
                if ("StressTest".equalsIgnoreCase(risk.getType()) && risk.getValue() != null) {
                    totalStress += risk.getValue();
                }
            }
        }

        return totalStress;
    }

    /**
     * Retrieve all risks in a portfolio (VaR, StressTest, others)
     *
     * @param portfolio the portfolio
     * @return list of all risks
     */
    public List<Risk> getAllPortfolioRisks(Portfolio portfolio) {
        List<Risk> allRisks = new ArrayList<>();

        if (portfolio == null || portfolio.getAssets() == null) return allRisks;

        for (Asset asset : portfolio.getAssets()) {
            if (asset.getRisks() != null) {
                allRisks.addAll(asset.getRisks());
            }
        }

        return allRisks;
    }

    // You can add other portfolio-related methods here later
}
