package com.sakhiya.investment.portfoliomanagement;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sakhiya.investment.clientmanagement.Client;
import com.sakhiya.investment.clientmanagement.ClientRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioRepository portfolioRepository;
    private final ClientRepository clientRepository;
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioRepository portfolioRepository, ClientRepository clientRepository, PortfolioService portfolioService) {
        this.portfolioRepository = portfolioRepository;
        this.clientRepository = clientRepository;
        this.portfolioService = portfolioService;
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

    // Create new portfolio }
    // Accept the DTO, not the entity, in controller. Fetch the related entity e.g Client using the ID from the DTO.
    //create method in sets all the fields from  PortfolioCreateDTO onto the Portfolio entity before saving.
    // call those methods to transfer data from the DTO to the entity before saving
    @PostMapping
    public ResponseEntity<?> createPortfolio(@RequestBody PortfolioCreateDTO dto) {
        Optional<Client> clientOpt = clientRepository.findById(dto.clientId);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Client not found");
        }
        Portfolio portfolio = new Portfolio();
        portfolio.setPortfolioName(dto.portfolioName);
        portfolio.setClient(clientOpt.get());
        portfolio.setInvestmentGoal(dto.investmentGoal);
        portfolio.setRiskLevel(dto.riskLevel);
        portfolio.setTotalValue(dto.totalValue);
        if (dto.createdAt != null) {
            portfolio.setCreatedAt(java.time.LocalDate.parse(dto.createdAt));
        }
        if (dto.updatedAt != null) {
            portfolio.setUpdatedAt(java.time.LocalDate.parse(dto.updatedAt));
        }
        Portfolio saved = portfolioRepository.save(portfolio);
        return ResponseEntity.ok(saved);
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
    public List<Portfolio> getPortfoliosByClientId(@PathVariable Client client) {
        return portfolioRepository.findByClient(client);
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


    // Get total risk value (sum of all risk values) for a portfolio by ID
    @GetMapping("/{portfolioId}/total-risk")
    public double getTotalRiskForPortfolio(@PathVariable String portfolioId) {
        Optional<Portfolio> portfolioOpt = portfolioRepository.findById(portfolioId);
        if (portfolioOpt.isEmpty()) {
            throw new IllegalArgumentException("Portfolio with id " + portfolioId + " not found");
        }
        return portfolioService.calculateTotalRisk(portfolioOpt.get());
    }



    
}
