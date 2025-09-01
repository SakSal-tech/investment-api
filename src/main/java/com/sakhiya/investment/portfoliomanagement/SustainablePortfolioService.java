package com.sakhiya.investment.portfoliomanagement;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for managing SustainablePortfolio entities.
 * Encapsulates business logic, validation, and interaction with the repository.
 */
@Service
public class SustainablePortfolioService {

    private final SustainablePortfolioRepository repository;

    // Constructor Injection (preferred over field injection)
    public SustainablePortfolioService(SustainablePortfolioRepository repository) {
        this.repository = repository;
    }

    /**
     * Create and persist a new SustainablePortfolio.
     * - Validates the incoming portfolio data.
     * - Sets createdAt and updatedAt timestamps.
     */
    public SustainablePortfolio createPortfolio(SustainablePortfolio portfolio) {
    validatePortfolio(portfolio);

    java.time.LocalDate today = java.time.LocalDate.now();
    portfolio.setCreatedAt(today);
    portfolio.setUpdatedAt(today);

    return repository.save(portfolio);
    }

    /**
     * Fetch all portfolios from the database.
     * Returns a list of all SustainablePortfolio objects.
     */
    public List<SustainablePortfolio> getAllPortfolios() {
        return repository.findAll();
    }

    /**
     * Fetch a portfolio by its ID.
     * Throws IllegalArgumentException if no portfolio exists with that ID.
     */
    public SustainablePortfolio getPortfolioById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with id: " + id));
    }

    /**
     * Update an existing portfolio.
     * - Retrieves the portfolio from DB.
     * - Applies changes from the input object.
     * - Updates the timestamp.
     */
    @Transactional
    public SustainablePortfolio updatePortfolio(String id, SustainablePortfolio updatedData) {
        SustainablePortfolio existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found with id: " + id));

    validatePortfolio(updatedData);

    // Update fields (maps/lists are converted internally by entity)
    existing.setPortfolioName(updatedData.getPortfolioName());
    existing.setClient(updatedData.getClient());
    existing.setEsgScores(updatedData.getEsgScores());
    existing.setImpactTargets(updatedData.getImpactTargets());
    existing.setThemeFocus(updatedData.getThemeFocus());
    existing.setExcludedSectors(updatedData.getExcludedSectors());
    existing.setPreferredSectors(updatedData.getPreferredSectors());

    existing.setUpdatedAt(java.time.LocalDate.now());

    return repository.save(existing);
    }

    /**
     * Delete a portfolio by ID.
     * Throws IllegalArgumentException if the ID doesn't exist.
     */
    public void deletePortfolio(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Portfolio not found with id: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Validate a SustainablePortfolio object.
     * Ensures required fields are populated and data is sensible.
     */
    private void validatePortfolio(SustainablePortfolio portfolio) {

        if (portfolio.getPortfolioId() == null || portfolio.getPortfolioId().isBlank()) {
            throw new IllegalArgumentException("Portfolio ID is required");
        }
        if (portfolio.getPortfolioName() == null || portfolio.getPortfolioName().isBlank()) {
            throw new IllegalArgumentException("Portfolio name is required");
        }
        if (portfolio.getClient() == null) {
            throw new IllegalArgumentException("Client ID is required");
        }

        // Optionally validate ESG scores, targets, etc.
        if (portfolio.getEsgScores() != null) {
            portfolio.getEsgScores().forEach((key, value) -> {
                if (value < 0 || value > 100) {
                    throw new IllegalArgumentException("ESG score for " + key + " must be between 0 and 100");
                }
            });
        }
    }
}
