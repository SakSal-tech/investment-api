package com.sakhiya.investment.portfoliomanagement;

import com.sakhiya.investment.portfoliomanagement.asset.Asset;
import com.sakhiya.investment.portfoliomanagement.asset.AssetService;
import com.sakhiya.investment.riskmanagement.Risk;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
     * Also ensures any collections in SustainablePortfolio are initialized before
     * saving.
     * 
     * @param portfolio the portfolio to update
     */
    @Transactional // Marks the method as transactional: all database changes succeed or fail
                   // together
    public void updatePortfolioRiskTotals(Portfolio portfolio, AssetService assetService) {
        if (portfolio == null)
            return; // Safety check: exit if portfolio is null

        double totalVaR = 0.0; // Initialise total Value at Risk (VaR)
        double totalStress = 0.0; // Initialise total Stress Test risk

        if (portfolio.getAssets() != null) { // Check if portfolio has assets
            for (Asset asset : portfolio.getAssets()) { // Loop through each asset
                // Refactored: removed the old nested loops that summed VaR/StressTest for each asset
                // Removed lines:
                // for (Risk risk : asset.getRisks()) {
                // if (risk.getValue() != null) {
                // if ("VaR".equalsIgnoreCase(risk.getType())) {
                // totalVaR += risk.getValue();
                // } else if ("StressTest".equalsIgnoreCase(risk.getType())) {
                // totalStress += risk.getValue();
                // }
                // }
                // }
                //Reason for removal:
                // 1. Logic for summing risks already exists in
                // AssetService.getTotalRiskValueByType().
                // 2. Avoids duplicate code DRY (Don't Repeat Yourself) principle.
                // 3. Makes PortfolioService simpler and more readable KISS principle.
                // New code delegates calculation to AssetService:
                totalVaR += assetService.getTotalRiskValueByType(asset.getAssetId(), "VaR"); // DRY
                totalStress += assetService.getTotalRiskValueByType(asset.getAssetId(), "StressTest"); // DRY
            }
        }

        portfolio.setTotalVaR(totalVaR); // Save calculated totalVaR to the portfolio
        portfolio.setTotalStressTest(totalStress); // Save calculated totalStress to the portfolio

        // Pattern matching for instanceof. If portfolio is a SustainablePortfolio:
        if (portfolio instanceof SustainablePortfolio sustainable) {
            // Ensure all Map/List fields are initializsd before save to prevent JPA errors
            //// KISS: simple initialisation
            if (sustainable.getEsgScores() == null)
                sustainable.setEsgScores(new java.util.HashMap<>()); 
            if (sustainable.getImpactTargets() == null)
                sustainable.setImpactTargets(new java.util.HashMap<>()); 
            if (sustainable.getThemeFocus() == null)
                sustainable.setThemeFocus(new ArrayList<>()); 
            if (sustainable.getExcludedSectors() == null)
                sustainable.setExcludedSectors(new ArrayList<>()); 
            if (sustainable.getPreferredSectors() == null)
                sustainable.setPreferredSectors(new ArrayList<>()); 
        }

        portfolioRepository.save(portfolio); // Persist the portfolio with updated totals and initialized fields

    }

    public double calculateTotalVaR(Portfolio portfolio) {
        if (portfolio == null || portfolio.getAssets() == null)
            return 0.0;

        double totalVaR = 0.0;
        for (Asset asset : portfolio.getAssets()) {
            if (asset.getRisks() == null)
                continue;
            for (Risk risk : asset.getRisks()) {
                if ("VaR".equalsIgnoreCase(risk.getType()) && risk.getValue() != null) {
                    totalVaR += risk.getValue();
                }
            }
        }
        return totalVaR;
    }

    public double calculateTotalStressTest(Portfolio portfolio) {
        if (portfolio == null || portfolio.getAssets() == null)
            return 0.0;

        double totalStress = 0.0;
        for (Asset asset : portfolio.getAssets()) {
            if (asset.getRisks() == null)
                continue;
            for (Risk risk : asset.getRisks()) {
                if ("StressTest".equalsIgnoreCase(risk.getType()) && risk.getValue() != null) {
                    totalStress += risk.getValue();
                }
            }
        }
        return totalStress;
    }

    public List<Risk> getAllPortfolioRisks(Portfolio portfolio) {
        List<Risk> allRisks = new ArrayList<>();
        if (portfolio == null || portfolio.getAssets() == null)
            return allRisks;

        for (Asset asset : portfolio.getAssets()) {
            if (asset.getRisks() != null) {
                allRisks.addAll(asset.getRisks());
            }
        }
        return allRisks;
    }

    public List<Portfolio> getAllClients() {
        return portfolioRepository.findAll();
    }

    public Portfolio getPortfolio(String portfolioId) throws NoSuchElementException {
        // The id is a String (UUID) not int
        return portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Portfolio with this id: " + portfolioId + " is not found"));
    }

    public List<Portfolio> getClientsByEmailServer(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("clientId must not be null or blank");
        }
        return portfolioRepository.findByClientId(clientId);
    }

    public List<Portfolio> getPortfoliosdByInvestmentGoal(String investmentGoal) {
        if (investmentGoal == null || investmentGoal.isBlank()) {
            throw new IllegalArgumentException("investmentGoal must not be null or blank");
        }
        return portfolioRepository.findByInvestmentGoal(investmentGoal);
    }

    public List<Portfolio> getPortfoliosByRiskLevel(Integer riskLevel) {
        if (riskLevel == null) {
            throw new IllegalArgumentException("riskLevel must not be null");
        }
        return portfolioRepository.findByRiskLevel(riskLevel);
    }

    public List<Portfolio> getPortifolisByCreatedAtAfter(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }
        return portfolioRepository.findByCreatedAtAfter(date);
    }

    public List<Portfolio> findPortfoliosByTotalValueGreaterThan(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }
        return portfolioRepository.findByTotalValueGreaterThan(value);
    }

    public List<Portfolio> findPortfoliosByTotalVaRGreaterThan(Double totalVaR) {
        if (totalVaR == null) {
            throw new IllegalArgumentException("totalVaR must not be null");
        }
        return portfolioRepository.findByTotalVaRGreaterThan(totalVaR);
    }

    public List<Portfolio> findPortfoliosByTotalStressTestGreaterThan(Double totalStressTest) {
        if (totalStressTest == null) {
            throw new IllegalArgumentException("totalStressTest must not be null");
        }
        return portfolioRepository.findByTotalStressTestGreaterThan(totalStressTest);
    }

}
