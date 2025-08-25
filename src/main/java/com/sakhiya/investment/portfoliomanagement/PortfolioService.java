package com.sakhiya.investment.portfoliomanagement;

import com.sakhiya.investment.portfoliomanagement.asset.Asset;
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
     * Also ensures any collections in SustainablePortfolio are initialized before saving.
     *
     * @param portfolio the portfolio to update
     */
    @Transactional
    public void updatePortfolioRiskTotals(Portfolio portfolio) {
        if (portfolio == null) return; // safety check

        double totalVaR = 0.0;
        double totalStress = 0.0;

        if (portfolio.getAssets() != null) {
            for (Asset asset : portfolio.getAssets()) {
                if (asset.getRisks() != null) {
                    for (Risk risk : asset.getRisks()) {
                        if (risk.getValue() != null) {
                            if ("VaR".equalsIgnoreCase(risk.getType())) {
                                totalVaR += risk.getValue();
                            } else if ("StressTest".equalsIgnoreCase(risk.getType())) {
                                totalStress += risk.getValue();
                            }
                        }
                    }
                }
            }
        }

        portfolio.setTotalVaR(totalVaR);
        portfolio.setTotalStressTest(totalStress);

        // pattern matching for instanceof.If portfolio is an instance of SustainablePortfolio
        // For SustainablePortfolio, ensure Map/List fields are initialized before save
        if (portfolio instanceof SustainablePortfolio sustainable) {
            if (sustainable.getEsgScores() == null) sustainable.setEsgScores(new java.util.HashMap<>());
            if (sustainable.getImpactTargets() == null) sustainable.setImpactTargets(new java.util.HashMap<>());
            if (sustainable.getThemeFocus() == null) sustainable.setThemeFocus(new ArrayList<>());
            if (sustainable.getExcludedSectors() == null) sustainable.setExcludedSectors(new ArrayList<>());
            if (sustainable.getPreferredSectors() == null) sustainable.setPreferredSectors(new ArrayList<>());
        }
        // This prevents JPA from throwing errors when persisting null collections

        portfolioRepository.save(portfolio);
    }

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
