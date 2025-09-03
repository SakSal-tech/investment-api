package com.sakhiya.investment.portfoliomanagement.asset;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import com.sakhiya.investment.riskmanagement.dto.RiskSummaryDTO;
@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    // Constructor injection of service
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    // Get all assets
    @GetMapping
    public List<Asset> getAllAssets() {
        return assetService.getAllAssets();
    }

    // Get asset by ID
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable String id) {
        Optional<Asset> assetOpt = assetService.getAssetById(id);

        // - If asset exists → return ResponseEntity.ok(asset)
        // - If asset does not exist → return ResponseEntity.notFound().build()
        if (assetOpt.isPresent()) {//isPresent() returns true if the Optional contains a non-null value.
            return ResponseEntity.ok(assetOpt.get()); // Return 200 OK with asset
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        }
    }

    // Create new asset
    @PostMapping
    public Asset createAsset(@RequestBody Asset asset) {
        // Simply save the asset using the service (which includes validation)
        return assetService.createAsset(asset);
    }

    // Update asset
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable String id, @RequestBody Asset updatedAsset) {
        Optional<Asset> assetOpt = assetService.updateAsset(id, updatedAsset);

        // if asset exists, return updated asset
        if (assetOpt.isPresent()) {
            return ResponseEntity.ok(assetOpt.get()); // Return 200 OK with saved asset
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found
        }
    }

    // Delete asset
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable String id) {
        if (assetService.deleteAsset(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Find assets by name
    @GetMapping("/name/{name}")
    public List<Asset> getAssetsByName(@PathVariable String name) {
        return assetService.getAssetsByName(name);
    }

    // Find assets by value >= given value
    @GetMapping("/value/{value}")
    public List<Asset> getAssetsByValue(@PathVariable Double value) {
        return assetService.getAssetsByValue(value);
    }

    // Find assets by portfolio ID
    @GetMapping("/portfolio/{portfolioId}")
    public List<Asset> getAssetsByPortfolioId(@PathVariable String portfolioId) {
        return assetService.getAssetsByPortfolioId(portfolioId);
    }

    // Find assets by value <= given value
    @GetMapping("/value/max/{value}")
    public List<Asset> getAssetsByValueMax(@PathVariable Double value) {
        return assetService.getAssetsByValueMax(value);
    }

    // Find assets by value between min and max
    @GetMapping("/value/between/{min}/{max}")
    public List<Asset> getAssetsByValueBetween(@PathVariable Double min, @PathVariable Double max) {
        return assetService.getAssetsByValueBetween(min, max);
    }

    // Find assets by name containing substring (case-insensitive)
    // To search for all assets whose name contains a given substring, ignoring upper/lowercase differences
    // e.g Stock /api/assets/name/contains/stock → returns "Apple Stock" and "Google Stock"
    @GetMapping("/name/contains/{substring}")
    public List<Asset> getAssetsByNameContaining(@PathVariable String substring) {
        return assetService.getAssetsByNameContaining(substring);
    }

    // Find assets by exact value
    @GetMapping("/value/exact/{value}")
    public List<Asset> getAssetsByExactValue(@PathVariable Double value) {
        return assetService.getAssetsByExactValue(value);
    }

    // Count assets by portfolio ID
    @GetMapping("/portfolio/{portfolioId}/count")
    public long countAssetsByPortfolioId(@PathVariable String portfolioId) {
        return assetService.countAssetsByPortfolioId(portfolioId);
    }

    // Find assets that have a specific risk by risk type
    @GetMapping("/risk-type/{type}")
    public List<Asset> getAssetsByRiskType(@PathVariable String type) {
        return assetService.getAssetsByRiskType(type);
    }

    // Find assets that have any risks
    @GetMapping("/with-risks")
    public List<Asset> getAssetsWithRisks() {
        return assetService.getAssetsWithRisks();
    }

    // Find assets with no risks
    @GetMapping("/without-risks")
    public List<Asset> getAssetsWithoutRisks() {
        return assetService.getAssetsWithoutRisks();
    }

    // Get total risk value by type for a specific asset
    @GetMapping("/{assetId}/total-risk/{riskType}")
    public double getTotalRiskByType(@PathVariable String assetId, @PathVariable String riskType) {
        return assetService.getTotalRiskValueByType(assetId, riskType);
    }

    // Get total risk summary (VaR and StressTest) for a specific asset
    @GetMapping("/{assetId}/total-risk-summary")
    public RiskSummaryDTO getTotalRiskSummary(@PathVariable String assetId) {
        return assetService.getTotalRiskSummary(assetId);
    }

    
}
