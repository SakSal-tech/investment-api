package com.sakhiya.investment.riskmanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/risks")
public class RiskController {

    private final RiskRepository riskRepository;

    public RiskController(RiskRepository riskRepository) {
        this.riskRepository = riskRepository;
    }

    // Get all risks
    @GetMapping
    public List<Risk> getAllRisks() {
        return riskRepository.findAll();
    }

    // Get risk by ID
    @GetMapping("/{id}")
    public ResponseEntity<Risk> getRiskById(@PathVariable UUID id) {
        Optional<Risk> risk = riskRepository.findById(id);
        return risk.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create new risk
    @PostMapping
    public Risk createRisk(@RequestBody Risk risk) {
        return riskRepository.save(risk);
    }

    // Update risk
    @PutMapping("/{id}")
    public ResponseEntity<Risk> updateRisk(@PathVariable UUID id, @RequestBody Risk updatedRisk) {
        return riskRepository.findById(id)
                .map(existingRisk -> {
                    // ID is auto-generated and should not be changed
                    Risk saved = riskRepository.save(updatedRisk);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete risk
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRisk(@PathVariable UUID id) {
        if (riskRepository.existsById(id)) {
            riskRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Find by type
    @GetMapping("/type/{type}")
    public List<Risk> getRisksByType(@PathVariable String type) {
        return riskRepository.findByType(type);
    }

    // Find by asset ID
    @GetMapping("/asset/{assetId}")
    public List<Risk> getRisksByAssetId(@PathVariable UUID assetId) {
        return riskRepository.findByAsset_AssetId(assetId);
    }

    // Find by calculation date
    @GetMapping("/date/{date}")
    public List<Risk> getRisksByCalculationDate(@PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return riskRepository.findByCalculationDate(parsedDate);
    }

    // Find by value greater than or equal to
    @GetMapping("/value/{value}")
    public List<Risk> getRisksByValue(@PathVariable Double value) {
        return riskRepository.findByValueGreaterThanEqual(value);
    }

    // Find by confidence level
    @GetMapping("/confidence/{confidenceLevel}")
    public List<Risk> getRisksByConfidenceLevel(@PathVariable Double confidenceLevel) {
        return riskRepository.findByConfidenceLevel(confidenceLevel);
    }

    // Find by scenario
    @GetMapping("/scenario/{scenario}")
    public List<Risk> getRisksByScenario(@PathVariable String scenario) {
        return riskRepository.findByScenario(scenario);
    }

    // Find by currency
    @GetMapping("/currency/{currency}")
    public List<Risk> getRisksByCurrency(@PathVariable String currency) {
        return riskRepository.findByCurrency(currency);
    }
}
