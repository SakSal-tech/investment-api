package com.sakhiya.investment.portfoliomanagement.asset;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetRepository assetRepository;

    public AssetController(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    // Get all assets
    @GetMapping
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    // Get asset by ID
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable UUID id) {
        Optional<Asset> asset = assetRepository.findById(id);
        return asset.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create new asset
    @PostMapping
    public Asset createAsset(@RequestBody Asset asset) {
        return assetRepository.save(asset);
    }

    // Update asset
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable UUID id, @RequestBody Asset updatedAsset) {
        return assetRepository.findById(id)
                .map(existingAsset -> {
                    Asset saved = assetRepository.save(updatedAsset);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete asset
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable UUID id) {
        if (assetRepository.existsById(id)) {
            assetRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Find assets by name
    @GetMapping("/name/{name}")
    public List<Asset> getAssetsByName(@PathVariable String name) {
        return assetRepository.findByName(name);
    }

    // Find assets by value greater than or equal to
    @GetMapping("/value/{value}")
    public List<Asset> getAssetsByValue(@PathVariable Double value) {
        return assetRepository.findByValueGreaterThanEqual(value);
    }

    // Find assets by portfolio ID
    @GetMapping("/portfolio/{portfolioId}")
    public List<Asset> getAssetsByPortfolioId(@PathVariable String portfolioId) {
        return assetRepository.findByPortfolio_PortfolioId(portfolioId);
    }

        // Find assets by value less than or equal to
    @GetMapping("/value/max/{value}")
    public List<Asset> getAssetsByValueMax(@PathVariable Double value) {
        return assetRepository.findByValueLessThanEqual(value);
    }

    // Find assets by value between two values
    @GetMapping("/value/between/{min}/{max}")
    public List<Asset> getAssetsByValueBetween(@PathVariable Double min, @PathVariable Double max) {
        return assetRepository.findByValueBetween(min, max);
    }

    // Find assets by name containing substring (case-insensitive)
    @GetMapping("/name/contains/{substring}")
    public List<Asset> getAssetsByNameContaining(@PathVariable String substring) {
        return assetRepository.findByNameContainingIgnoreCase(substring);
    }

    // Find assets by exact value
    @GetMapping("/value/exact/{value}")
    public List<Asset> getAssetsByExactValue(@PathVariable Double value) {
        return assetRepository.findByValue(value);
    }

    // Count assets by portfolio ID
    @GetMapping("/portfolio/{portfolioId}/count")
    public long countAssetsByPortfolioId(@PathVariable String portfolioId) {
        return assetRepository.countByPortfolio_PortfolioId(portfolioId);
    }

        // Find assets that have a specific risk by risk type
    @GetMapping("/risk-type/{type}")
    public List<Asset> getAssetsByRiskType(@PathVariable String type) {
        return assetRepository.findByRisks_Type(type);
    }

    // Find assets that have any risks
    @GetMapping("/with-risks")
    public List<Asset> getAssetsWithRisks() {
        return assetRepository.findByRisksIsNotNull();
    }

    // Find assets with no risks
    @GetMapping("/without-risks")
    public List<Asset> getAssetsWithoutRisks() {
        return assetRepository.findByRisksIsNull();
    }


}
