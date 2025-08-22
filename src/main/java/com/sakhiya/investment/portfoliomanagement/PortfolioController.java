package com.sakhiya.investment.portfoliomanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioRepository portfolioRepository;

    
    public PortfolioController(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    // Get all portfolios
    @GetMapping
    public List<Portfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    // Get portfolio by ID
    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> getPortfolioById(@PathVariable String id) {
        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
        return portfolio.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create new portfolio
    @PostMapping
    public Portfolio createPortfolio(@RequestBody Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    // Update portfolio
    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> updatePortfolio(@PathVariable String id, @RequestBody Portfolio updatedPortfolio) {
        return portfolioRepository.findById(id)
                .map(existingPortfolio -> {
                    updatedPortfolio.setPortfolioId(id);
                    Portfolio saved = portfolioRepository.save(updatedPortfolio);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete portfolio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable String id) {
        if (portfolioRepository.existsById(id)) {
            portfolioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Find portfolios by clientId
    @GetMapping("/client/{clientId}")
    public List<Portfolio> getPortfoliosByClientId(@PathVariable String clientId) {
        return portfolioRepository.findByClientId(clientId);
    }

    // Find portfolios by investment goal
    @GetMapping("/goal/{goal}")
    public List<Portfolio> getPortfoliosByInvestmentGoal(@PathVariable String goal) {
        return portfolioRepository.findByInvestmentGoal(goal);
    }

    // Find portfolios by risk level
    @GetMapping("/risk/{riskLevel}")
    public List<Portfolio> getPortfoliosByRiskLevel(@PathVariable Integer riskLevel) {
        return portfolioRepository.findByRiskLevel(riskLevel);
    }

    // Find portfolios by total VaR greater than a value
    @GetMapping("/total-var/{totalVaR}")
    public List<Portfolio> getPortfoliosByTotalVaR(@PathVariable Double totalVaR) {
        return portfolioRepository.findByTotalVaRGreaterThan(totalVaR);
    }

    // Find portfolios by total Stress Test greater than a value
    @GetMapping("/total-stress/{totalStressTest}")
    public List<Portfolio> getPortfoliosByTotalStressTest(@PathVariable Double totalStressTest) {
        return portfolioRepository.findByTotalStressTestGreaterThan(totalStressTest);
    }
}
