package com.sakhiya.investment.portfoliomanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sakhiya.investment.clientmanagement.Client;
import com.sakhiya.investment.clientmanagement.ClientRepository;
import com.sakhiya.investment.portfoliomanagement.asset.Asset;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioRepository portfolioRepository;
    private final ClientRepository clientRepository;
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioRepository portfolioRepository, ClientRepository clientRepository,
            PortfolioService portfolioService) {
        this.portfolioRepository = portfolioRepository;
        this.clientRepository = clientRepository;
        this.portfolioService = portfolioService;
    }

    // Helper method to map Portfolio to PortfolioDTO
    private PortfolioDTO toDTO(Portfolio portfolio) {
        PortfolioDTO dto = new PortfolioDTO();
        dto.setPortfolioId(portfolio.getPortfolioId());
        dto.setPortfolioName(portfolio.getPortfolioName());
        dto.setCreatedAt(portfolio.getCreatedAt());
        dto.setUpdatedAt(portfolio.getUpdatedAt());
        dto.setInvestmentGoal(portfolio.getInvestmentGoal());
        dto.setRiskLevel(portfolio.getRiskLevel());
        dto.setTotalValue(portfolio.getTotalValue());

        // FIX: Always calculate latest values for VaR and StressTest
        dto.setTotalVaR(portfolioService.calculateTotalVaR(portfolio));
        dto.setTotalStressTest(portfolioService.calculateTotalStressTest(portfolio));

        if (portfolio.getClient() != null) {
            String fullName = portfolio.getClient().getFirstName() + " " + portfolio.getClient().getSurname();
            dto.setClientName(fullName);
            dto.setClientId(portfolio.getClient().getClientId());
        }

        if (portfolio.getAssets() != null && !portfolio.getAssets().isEmpty()) {
            dto.setAssetCount(portfolio.getAssets().size());
            dto.setAssetNames(portfolio.getAssets().stream().map(Asset::getName).toList());
            dto.setAssetIds(portfolio.getAssets().stream().map(Asset::getAssetId).toList());
        } else {
            dto.setAssetCount(0);
            dto.setAssetNames(List.of());
            dto.setAssetIds(List.of());
        }

        return dto;
    }

    // Get all portfolios as DTOs
    @GetMapping
    public List<PortfolioDTO> getAllPortfolios() {
        List<Portfolio> portfolios = portfolioRepository.findAll();
        return portfolios.stream()
                .map(this::toDTO)
                .toList();
    }

    // Get portfolio by ID as DTO
    @GetMapping("/{id}")
    public ResponseEntity<PortfolioDTO> getPortfolioById(@PathVariable String id) {
        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
        return portfolio.map(p -> ResponseEntity.ok(toDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create new portfolio }
    // Accept the DTO, not the entity, in controller. Fetch the related entity e.g
    // Client using the ID from the DTO.
    // create method in sets all the fields from PortfolioCreateDTO onto the
    // Portfolio entity before saving.
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
    public ResponseEntity<PortfolioDTO> updatePortfolio(
            @PathVariable String id,
            @RequestBody PortfolioDTO dto) {

        return portfolioRepository.findById(id)
                .map(existing -> {
                    // Update simple fields
                    if (dto.getPortfolioName() != null)
                        existing.setPortfolioName(dto.getPortfolioName());
                    if (dto.getInvestmentGoal() != null)
                        existing.setInvestmentGoal(dto.getInvestmentGoal());
                    if (dto.getRiskLevel() != null)
                        existing.setRiskLevel(dto.getRiskLevel());
                    if (dto.getTotalValue() != null)
                        existing.setTotalValue(dto.getTotalValue());
                    if (dto.getTotalVaR() != null)
                        existing.setTotalVaR(dto.getTotalVaR());
                    if (dto.getTotalStressTest() != null)
                        existing.setTotalStressTest(dto.getTotalStressTest());

                    // Update client if clientId is provided
                    if (dto.getClientId() != null) {
                        clientRepository.findById(dto.getClientId())
                                .ifPresent(existing::setClient);
                    }

                    // Update assets if assetIds provided
                    if (dto.getAssetIds() != null) {
                        List<Asset> assets = dto.getAssetIds().stream()
                                .map(assetId -> portfolioService.getAssetById(assetId)) // service fetches Asset entity
                                .toList();
                        existing.setAssets(assets);
                    }

                    existing.setUpdatedAt(java.time.LocalDate.now());

                    Portfolio saved = portfolioRepository.save(existing);
                    return ResponseEntity.ok(toDTO(saved));
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

    // Find portfolios by clientId. GetMapping("/client/{clientId}") method is to
    // accept the client ID as a String and fetch the Client entity inside the
    // method, changed instead of using @PathVariable Client client.
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<PortfolioDTO>> getPortfoliosByClientId(@PathVariable String clientId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<PortfolioDTO> dtos = portfolioRepository.findByClient(clientOpt.get())
                .stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // Find portfolios by investment goal
    @GetMapping("/goal/{goal}")
    public List<PortfolioDTO> getPortfoliosByInvestmentGoal(@PathVariable String goal) {
        return portfolioRepository.findByInvestmentGoal(goal)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Find portfolios by risk level
    @GetMapping("/risk/{riskLevel}")
    public List<PortfolioDTO> getPortfoliosByRiskLevel(@PathVariable Integer riskLevel) {
        return portfolioRepository.findByRiskLevel(riskLevel)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Calculate total VaR for a portfolio by ID
    @GetMapping("/{portfolioId}/total-var")
    public ResponseEntity<Double> getTotalVaRForPortfolio(@PathVariable String portfolioId) {
        Optional<Portfolio> portfolioOpt = portfolioRepository.findById(portfolioId);
        if (portfolioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        double totalVaR = portfolioService.calculateTotalVaR(portfolioOpt.get());
        return ResponseEntity.ok(totalVaR);
    }

    // Calculate total Stress Test for a portfolio by ID
    @GetMapping("/{portfolioId}/total-stress")
    public ResponseEntity<Double> getTotalStressTestForPortfolio(@PathVariable String portfolioId) {
        Optional<Portfolio> portfolioOpt = portfolioRepository.findById(portfolioId);
        if (portfolioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        double totalStressTest = portfolioService.calculateTotalStressTest(portfolioOpt.get());
        return ResponseEntity.ok(totalStressTest);
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