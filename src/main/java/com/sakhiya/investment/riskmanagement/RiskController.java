package com.sakhiya.investment.riskmanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/risks")
public class RiskController {

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    // Get all risks
    @GetMapping
    public List<Risk> getAllRisks() {
        return riskService.getAllRisks();
    }

    // Get risk by ID
    @GetMapping("/{id}")
    public ResponseEntity<Risk> getRiskById(@PathVariable String id) {
        Risk risk = riskService.getRiskById(id);
        return ResponseEntity.ok(risk);
    }
    // Create risk
    @PostMapping
    public Risk createRisk(@RequestBody Risk risk) {
        return riskService.createRisk(risk);
    }

    // Update risk
    /**
     * Updates an existing Risk by its ID.
     * @param id The ID of the risk to update (from the URL path)
     * @param updatedRisk The new risk data (from the request body)
     * @return 200 OK with updated risk if successful, 404 Not Found if not
     * @throws NoSuchElementException if the risk with the given ID is not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Risk> updateRisk(@PathVariable String id, @RequestBody Risk updatedRisk) {
        try {
            Risk risk = riskService.updateRisk(id, updatedRisk);
            return ResponseEntity.ok(risk);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete risk
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRisk(@PathVariable String id) {
        try {
            riskService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    


    // Find by type
    @GetMapping("/type/{type}")
    public List<Risk> getRisksByType(@PathVariable String type) {
        return riskService.getByType(type);
    }

    // Find by asset ID
    @GetMapping("/asset/{assetId}")
    public List<Risk> getRisksByAssetId(@PathVariable String assetId) {
        return riskService.getByAssetId(assetId);
    }

    // Find by calculation date
    @GetMapping("/date/{date}")
    public List<Risk> getRisksByCalculationDate(@PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return riskService.getByCalculationDate(parsedDate);
    }

    // Find by value greater than or equal to
    @GetMapping("/value/{value}")
    public List<Risk> getRisksByValue(@PathVariable double value) {
        return riskService.getByValueGreaterThanEqual(value);
    }

    // Find by confidence level
    @GetMapping("/confidence/{confidenceLevel}")
    public List<Risk> getRisksByConfidenceLevel(@PathVariable double confidenceLevel) {
        return riskService.getByConfidenceLevel(confidenceLevel);
    }

    // Find by scenario
    @GetMapping("/scenario/{scenario}")
    public List<Risk> getRisksByScenario(@PathVariable String scenario) {
        return riskService.getByScenario(scenario);
    }

    // Find by currency
    @GetMapping("/currency/{currency}")
    public List<Risk> getRisksByCurrency(@PathVariable String currency) {
        return riskService.getByCurrency(currency);
    }

    
    
}
