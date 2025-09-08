package com.sakhiya.investment.riskmanagement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sakhiya.investment.portfoliomanagement.asset.Asset;
import com.sakhiya.investment.portfoliomanagement.asset.AssetService;

@RestController
@RequestMapping("/api/risks")
public class RiskController {

    private final RiskService riskService;
    private final AssetService assetService;

    public RiskController(RiskService riskService, AssetService assetService) {
        this.riskService = riskService;
        this.assetService = assetService;
    }

    @PostMapping("/var")
    public ResponseEntity<Map<String, Object>> calculateVaR(
            @RequestParam String assetId,
            @RequestParam double confidenceLevel,
            @RequestParam int timeHorizonDays) {
        assetService.getAssetById(assetId)
                .orElseThrow(() -> new NoSuchElementException("Asset with id " + assetId + " not found"));
        Risk risk = riskService.createAndSaveVaR(assetId, confidenceLevel, timeHorizonDays);

        // Refactored: Deserialize detailsJson to JSON object for Postman presentation
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> details = null;
        try {
            details = mapper.readValue(risk.getDetailsJson(), new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        /*Refactored: Return Risk + parsed details as a JSON object response is a map with two keys:
         "risk" and "details". This allows clients (like Postman or your frontend) 
         to see both the main risk data and the extra details (which were originally stored as a JSON string) 
         as a proper JSON object, making it easier to work with in API consumers.
         */ 
        Map<String, Object> response = new HashMap<>();
        response.put("risk", risk);
        response.put("details", details);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/stress-test")
    public ResponseEntity<Map<String, Object>> calculateStressTest(
            @RequestParam String assetId,
            @RequestParam String scenario) {
        Asset asset = assetService.getAssetById(assetId)
                .orElseThrow(() -> new NoSuchElementException("Asset with id " + assetId + " not found"));
        Risk risk = riskService.stressTestCalculator(asset, scenario);

        /**Refactored: to Deserialize detailsJson to JSON object for Postman presentation
         * code is converting the detailsJson field (which is a JSON string stored in the Risk object) 
         * into a real Java Map object.
          */ 
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> details = null;
        try {
            details = mapper.readValue(risk.getDetailsJson(), new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        // Refactored: Return Risk + parsed details as a JSON object
        Map<String, Object> response = new HashMap<>();
        response.put("risk", risk);
        response.put("details", details);

        return ResponseEntity.ok(response);
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

    @PutMapping("/{id}")
    public ResponseEntity<Risk> updateRisk(@PathVariable String id, @RequestBody Risk updatedRisk) {
        try {
            Risk risk = riskService.updateRisk(id, updatedRisk);
            return ResponseEntity.ok(risk);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRisk(@PathVariable String id) {
        try {
            riskService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/type/{type}")
    public List<Risk> getRisksByType(@PathVariable String type) {
        return riskService.getByType(type);
    }

    @GetMapping("/asset/{assetId}")
    public List<Risk> getRisksByAssetId(@PathVariable String assetId) {
        return riskService.getByAssetId(assetId);
    }

    @GetMapping("/date/{date}")
    public List<Risk> getRisksByCalculationDate(@PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return riskService.getByCalculationDate(parsedDate);
    }

    @GetMapping("/value/{value}")
    public List<Risk> getRisksByValue(@PathVariable double value) {
        return riskService.getByValueGreaterThanEqual(value);
    }

    @GetMapping("/confidence/{confidenceLevel}")
    public List<Risk> getRisksByConfidenceLevel(@PathVariable double confidenceLevel) {
        return riskService.getByConfidenceLevel(confidenceLevel);
    }

    @GetMapping("/scenario/{scenario}")
    public List<Risk> getRisksByScenario(@PathVariable String scenario) {
        return riskService.getByScenario(scenario);
    }

    @GetMapping("/currency/{currency}")
    public List<Risk> getRisksByCurrency(@PathVariable String currency) {
        return riskService.getByCurrency(currency);
    }
}
