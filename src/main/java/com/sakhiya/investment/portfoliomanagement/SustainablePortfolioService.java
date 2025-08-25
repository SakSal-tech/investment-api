package com.sakhiya.investment.portfoliomanagement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
// Used to define the expected date format (e.g., yyyy-MM-dd) for parsing date strings
import java.util.List;
import com.sakhiya.investment.util.Validations;

@Service
public class SustainablePortfolioService {
    private final SustainablePortfolioRepository repository;

    public SustainablePortfolioService(SustainablePortfolioRepository repository) {
        this.repository = repository;
    }

    // Create a new SustainablePortfolio
    public SustainablePortfolio createPortfolio(SustainablePortfolio portfolio) {
        validatePortfolio(portfolio);
        return repository.save(portfolio);
    }

    // Get all portfolios
    public List<SustainablePortfolio> getAllPortfolios() {
        return repository.findAll();
    }

    // Get by ID with exception if not found
    public SustainablePortfolio getPortfolioById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with id: " + id));
    }

    // Update an existing portfolio
    @Transactional
    public SustainablePortfolio updatePortfolio(String id, SustainablePortfolio updated) {
        validatePortfolio(updated);
        SustainablePortfolio current = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with id: " + id));
        // Update fields (add more as needed)
        current.setPortfolioName(updated.getPortfolioName());
        current.setClientId(updated.getClientId());
        current.setCreatedAt(updated.getCreatedAt());
        current.setUpdatedAt(updated.getUpdatedAt());
        current.setInvestmentGoal(updated.getInvestmentGoal());
        current.setRiskLevel(updated.getRiskLevel());
        current.setTotalValue(updated.getTotalValue());
        current.setAssets(updated.getAssets());
        current.setEsgScores(updated.getEsgScores());
        current.setThemeFocus(updated.getThemeFocus());
        current.setImpactTargets(updated.getImpactTargets());
        current.setOverallEsgScore(updated.getOverallEsgScore());
        current.setExcludedSectors(updated.getExcludedSectors());
        current.setPreferredSectors(updated.getPreferredSectors());
        current.setLastUpdated(updated.getLastUpdated());
        current.setComplianceStatus(updated.getComplianceStatus());
        // Save is not needed with @Transactional if entity is managed
        return current;
    }

    // Delete by ID
    public void deletePortfolio(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Portfolio not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // Custom queries with validation
    public List<SustainablePortfolio> findByComplianceStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Compliance status must not be blank");
        }
        return repository.findByComplianceStatus(status);
    }

    public List<SustainablePortfolio> findByThemeFocus(String theme) {
        if (theme == null || theme.isBlank()) {
            throw new IllegalArgumentException("Theme must not be blank");
        }
        // return repository.findByThemeFocusContaining(theme); // COMMENTED OUT: JPA cannot query elements within a List<String> field using 'Containing' for collections
        return java.util.Collections.emptyList();
    }

    // public List<SustainablePortfolio> findByExcludedSector(String sector) {
    //     if (sector == null || sector.isBlank()) {
    //         throw new IllegalArgumentException("Sector must not be blank");
    //     }
    //     return repository.findByExcludedSectorsContaining(sector);
    // }

    // public List<SustainablePortfolio> findByPreferredSector(String sector) {
    //     if (sector == null || sector.isBlank()) {
    //         throw new IllegalArgumentException("Sector must not be blank");
    //     }
    //     return repository.findByPreferredSectorsContaining(sector);
    // }

    public List<SustainablePortfolio> findByEsgScoreGreaterThanEqual(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("ESG score must be non-negative");
        }
        return repository.findByOverallEsgScoreGreaterThanEqual(score);
    }

    // public List<SustainablePortfolio> findByLastUpdatedAfter(LocalDate date) {
    //     if (date == null) {
    //         throw new IllegalArgumentException("Date must not be null");
    //     }
    //     return repository.findByLastUpdatedAfter(date);
    // }

    // public List<SustainablePortfolio> findByImpactTargetKey(String key) {
    //     if (key == null || key.isBlank()) {
    //         throw new IllegalArgumentException("Impact target key must not be blank");
    //     }
    //     return repository.findByImpactTargetsKey(key);
    // }

    // public List<SustainablePortfolio> findByEsgScoresKey(String key) {
    //     if (key == null || key.isBlank()) {
    //         throw new IllegalArgumentException("ESG score key must not be blank");
    //     }
    //     return repository.findByEsgScoresKey(key);
    // }

    // Validation logic for SustainablePortfolio to centralise and enforces the core validation rules for a SustainablePortfolio object in one place, rather than scattering them across multiple methods.
    private void validatePortfolio(SustainablePortfolio portfolio) {
        if (portfolio == null) {
            throw new IllegalArgumentException("Portfolio must not be null");
        }
        if (portfolio.getPortfolioName() == null || portfolio.getPortfolioName().isBlank()) {
            throw new IllegalArgumentException("Portfolio name must not be blank");
        }
        if (portfolio.getClientId() == null || portfolio.getClientId().isBlank()) {
            throw new IllegalArgumentException("Client ID must not be blank");
        }
        // Validate createdAt and updatedAt dates if present
        if (portfolio.getCreatedAt() != null && !Validations.isValidDate(portfolio.getCreatedAt().toString())) {
            throw new IllegalArgumentException("Invalid createdAt date format, expected yyyy-MM-dd");
        }
        if (portfolio.getUpdatedAt() != null && !Validations.isValidDate(portfolio.getUpdatedAt().toString())) {
            throw new IllegalArgumentException("Invalid updatedAt date format, expected yyyy-MM-dd");
        }
        // Add more validation as needed (e.g., riskLevel, totalValue, etc.)
    }

    // Use Validations.isValidDate(dateString) from com.sakhiya.investment.util.Validations for date validation across the application.
}
