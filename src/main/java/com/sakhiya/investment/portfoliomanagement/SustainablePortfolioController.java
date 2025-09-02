package com.sakhiya.investment.portfoliomanagement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

import com.sakhiya.investment.clientmanagement.*;;;

/**
 * REST Controller for SustainablePortfolio.
 * Delegates business logic to SustainablePortfolioService.
 */
// Allow cross-origin requests from any origin (not needed for Postman, but added for troubleshooting 403)
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/sustainable-portfolios")
public class SustainablePortfolioController {

    private final SustainablePortfolioService service;
    private final SustainablePortfolioRepository sustainablePortfolioRepository;
    private final ClientRepository clientRepository;

    public SustainablePortfolioController(SustainablePortfolioService service,
                                          SustainablePortfolioRepository sustainablePortfolioRepository,
                                          ClientRepository clientRepository) {
        this.service = service;
        this.sustainablePortfolioRepository = sustainablePortfolioRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * GET /api/sustainable-portfolios
     * Fetch all portfolios.
     */
    @GetMapping
    public List<SustainablePortfolio> getAllSustainablePortfolios() {
        return service.getAllPortfolios();
    }

    /**
     * GET /api/sustainable-portfolios/{id}
     * Fetch a portfolio by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SustainablePortfolio> getSustainablePortfolioById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.getPortfolioById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/sustainable-portfolios
     * Create a new portfolio.
     */
    @PostMapping
    public ResponseEntity<SustainablePortfolio> createSustainablePortfolio(@RequestBody SustainablePortfolioCreateDTO dto) {
        Optional<Client> clientOpt = clientRepository.findById(dto.clientId);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        SustainablePortfolio portfolio = new SustainablePortfolio(
            dto.portfolioName,
            clientOpt.get(),
            dto.createdAt != null ? LocalDate.parse(dto.createdAt) : null,
            dto.updatedAt != null ? LocalDate.parse(dto.updatedAt) : null,
            dto.investmentGoal,
            dto.riskLevel,
            dto.totalValue,
            null, // assets (not set at creation)
            dto.esgScores,
            dto.themeFocus,
            dto.impactTargets,
            dto.overallEsgScore,
            dto.excludedSectors,
            dto.preferredSectors,
            dto.lastUpdated != null ? LocalDate.parse(dto.lastUpdated) : null,
            dto.complianceStatus
        );
        SustainablePortfolio saved = sustainablePortfolioRepository.save(portfolio);
        return ResponseEntity.ok(saved);
    }

    /**
     * PUT /api/sustainable-portfolios/{id}
     * Update an existing portfolio.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SustainablePortfolio> updateSustainablePortfolio(
            @PathVariable String id,
            @RequestBody SustainablePortfolio updatedPortfolio) {

        try {
            return ResponseEntity.ok(service.updatePortfolio(id, updatedPortfolio));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/sustainable-portfolios/{id}
     * Delete a portfolio by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSustainablePortfolio(@PathVariable String id) {
        try {
            service.deletePortfolio(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/sustainable-portfolios/compliance/{status}
     * Find portfolios by compliance status (simple String field – works with JPA).
     */
    @GetMapping("/compliance/{status}")
    public List<SustainablePortfolio> getByComplianceStatus(@PathVariable String status) {
        return service.getAllPortfolios()
                .stream()
                .filter(p -> status.equalsIgnoreCase(p.getComplianceStatus()))
                .toList();
    }

    /**
     * GET /api/sustainable-portfolios/theme/{theme}
     * (In-memory filter, since JPA cannot query inside List<String>)
     */
    @GetMapping("/theme/{theme}")
    public List<SustainablePortfolio> getByThemeFocus(@PathVariable String theme) {
        return service.getAllPortfolios()
                .stream()
                .filter(p -> p.getThemeFocus() != null && p.getThemeFocus().contains(theme))
                .toList();
    }

    /**
     * GET /api/sustainable-portfolios/excluded-sector/{sector}
     * (In-memory filter, since excludedSectors is stored as JSON in DB)
     */
    @GetMapping("/excluded-sector/{sector}")
    public List<SustainablePortfolio> getByExcludedSector(@PathVariable String sector) {
        return service.getAllPortfolios()
                .stream()
                .filter(p -> p.getExcludedSectors() != null && p.getExcludedSectors().contains(sector))
                .toList();
    }

    /**
     * GET /api/sustainable-portfolios/esg-score/{score}
     * Query by numeric ESG score (simple DB-supported field).
     */
    @GetMapping("/esg-score/{score}")
    public List<SustainablePortfolio> getByEsgScore(@PathVariable int score) {
        return service.getAllPortfolios()
                .stream()
                .filter(p -> p.getOverallEsgScore() >= score)
                .toList();
    }

    // Find by last updated after a certain date (format: yyyy-MM-dd)
    @GetMapping("/last-updated-after/{date}")
    public List<SustainablePortfolio> getByLastUpdatedAfter(@PathVariable String date) {
        java.time.LocalDate parsedDate = java.time.LocalDate.parse(date);
        return sustainablePortfolioRepository.findByLastUpdatedAfter(parsedDate);
    }

    // Find by impact target key
    @GetMapping("/impact-target/{key}")
    public List<SustainablePortfolio> getByImpactTargetKey(@PathVariable String key) {
        return sustainablePortfolioRepository.findByImpactTargetKey(key);
    }

    // Find by ESG score key
    @GetMapping("/esg-score-key/{key}")
    public List<SustainablePortfolio> getByEsgScoreKey(@PathVariable String key) {
        return sustainablePortfolioRepository.findByEsgScoreKey(key);
    }

}
