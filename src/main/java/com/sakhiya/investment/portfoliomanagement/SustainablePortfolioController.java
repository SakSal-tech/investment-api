    // Find by excluded sector
package com.sakhiya.investment.portfoliomanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

// Allow cross-origin requests from any origin (not needed for Postman, but added for troubleshooting 403)
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/sustainable-portfolios")
public class SustainablePortfolioController {

    private final SustainablePortfolioRepository sustainablePortfolioRepository;

    public SustainablePortfolioController(SustainablePortfolioRepository sustainablePortfolioRepository) {
        this.sustainablePortfolioRepository = sustainablePortfolioRepository;
    }

    // Get all sustainable portfolios
    @GetMapping
    public List<SustainablePortfolio> getAllSustainablePortfolios() {
        return sustainablePortfolioRepository.findAll();
    }

    // Get sustainable portfolio by ID
    @GetMapping("/{id}")
    public ResponseEntity<SustainablePortfolio> getSustainablePortfolioById(@PathVariable String id) {
        Optional<SustainablePortfolio> portfolio = sustainablePortfolioRepository.findById(id);
        return portfolio.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create new sustainable portfolio
    @PostMapping
    public SustainablePortfolio createSustainablePortfolio(@RequestBody SustainablePortfolio portfolio) {
        return sustainablePortfolioRepository.save(portfolio);
    }

    // Update sustainable portfolio
    @PutMapping("/{id}")
    public ResponseEntity<SustainablePortfolio> updateSustainablePortfolio(@PathVariable String id, @RequestBody SustainablePortfolio updatedPortfolio) {
        return sustainablePortfolioRepository.findById(id)
                .map(existingPortfolio -> {
                    updatedPortfolio.setPortfolioId(id);
                    SustainablePortfolio saved = sustainablePortfolioRepository.save(updatedPortfolio);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete sustainable portfolio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSustainablePortfolio(@PathVariable String id) {
        if (sustainablePortfolioRepository.existsById(id)) {
            sustainablePortfolioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Find by compliance status
    @GetMapping("/compliance/{status}")
    public List<SustainablePortfolio> getByComplianceStatus(@PathVariable String status) {
        return sustainablePortfolioRepository.findByComplianceStatus(status);
    }

    // Find by theme focus
    @GetMapping("/theme/{theme}")
    public List<SustainablePortfolio> getByThemeFocus(@PathVariable String theme) {
        return sustainablePortfolioRepository.findByThemeFocusContaining(theme);
    }
        @GetMapping("/excluded-sector/{sector}")
    public List<SustainablePortfolio> getByExcludedSector(@PathVariable String sector) {
        return sustainablePortfolioRepository.findByExcludedSectorsContaining(sector);
    }

    // Find by preferred sector
    @GetMapping("/preferred-sector/{sector}")
    public List<SustainablePortfolio> getByPreferredSector(@PathVariable String sector) {
        return sustainablePortfolioRepository.findByPreferredSectorsContaining(sector);
    }

    // Find by ESG score greater than or equal to a value
    @GetMapping("/esg-score/{score}")
    public List<SustainablePortfolio> getByEsgScore(@PathVariable int score) {
        return sustainablePortfolioRepository.findByOverallEsgScoreGreaterThanEqual(score);
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
