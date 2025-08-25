package com.sakhiya.investment.portfoliomanagement.asset;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AssetService {
    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    // Returns all assets in the system
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    // Returns an asset by its unique UUID, or empty if not found
    public Optional<Asset> getAssetById(String id) {
        return assetRepository.findById(id);
    }

    // Validates and creates a new asset
    public Asset createAsset(Asset asset) {
        validateAsset(asset);
        return assetRepository.save(asset);
    }

    // Validates and updates an existing asset
    public Optional<Asset> updateAsset(String id, Asset updatedAsset) {
        validateAsset(updatedAsset);
        return assetRepository.findById(id).map(existingAsset -> {
            // Only update fields that are allowed to change
            existingAsset.setName(updatedAsset.getName());
            existingAsset.setValue(updatedAsset.getValue());
            existingAsset.setPortfolio(updatedAsset.getPortfolio());
            existingAsset.setRisks(updatedAsset.getRisks());
            // Add more fields as needed for your domain
            validateAsset(existingAsset); // Validate after update
            return assetRepository.save(existingAsset);
        });
    }

    // Deletes an asset by its UUID
    public boolean deleteAsset(String id) {
        if (assetRepository.existsById(id)) {
            assetRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Finds assets by exact name
    public List<Asset> getAssetsByName(String name) {
        return assetRepository.findByName(name);
    }

    // Finds assets with value >= given value
    public List<Asset> getAssetsByValue(Double value) {
        return assetRepository.findByValueGreaterThanEqual(value);
    }

    // Finds assets by portfolio ID
    public List<Asset> getAssetsByPortfolioId(String portfolioId) {
        return assetRepository.findByPortfolio_PortfolioId(portfolioId);
    }

    // Finds assets with value <= given value
    public List<Asset> getAssetsByValueMax(Double value) {
        return assetRepository.findByValueLessThanEqual(value);
    }

    // Finds assets with value between min and max
    public List<Asset> getAssetsByValueBetween(Double min, Double max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum value cannot be greater than maximum value");
        }
        return assetRepository.findByValueBetween(min, max);
    }

    // Finds assets by name containing substring (case-insensitive)
    public List<Asset> getAssetsByNameContaining(String substring) {
        if (substring == null || substring.isBlank()) {
            throw new IllegalArgumentException("Substring for asset name search cannot be blank");
        }
        return assetRepository.findByNameContainingIgnoreCase(substring);
    }

    // Finds assets by exact value
    public List<Asset> getAssetsByExactValue(Double value) {
        return assetRepository.findByValue(value);
    }

    // Counts assets by portfolio ID
    public long countAssetsByPortfolioId(String portfolioId) {
        return assetRepository.countByPortfolio_PortfolioId(portfolioId);
    }

    // Finds assets that have a specific risk by risk type
    public List<Asset> getAssetsByRiskType(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Risk type cannot be blank");
        }
        return assetRepository.findByRisks_Type(type);
    }

    // Finds assets that have any risks
    public List<Asset> getAssetsWithRisks() {
        return assetRepository.findByRisksIsNotNull();
    }

    // Finds assets with no risks
    public List<Asset> getAssetsWithoutRisks() {
        return assetRepository.findByRisksIsNull();
    }

    // Validates the asset object for required fields and business rules
    private void validateAsset(Asset asset) {
        // Asset name must not be null or blank, as it's a key identifier for users
        if (asset.getName() == null || asset.getName().isBlank()) {
            throw new IllegalArgumentException("Asset name cannot be blank");
        }
        // Asset value must be non-negative, as negative asset values don't make sense in this domain
        if (asset.getValue() == null || asset.getValue() < 0) {
            throw new IllegalArgumentException("Asset value must be non-negative");
        }
        // Portfolio must not be null, as every asset should belong to a portfolio in this system
        if (asset.getPortfolio() == null) {
            throw new IllegalArgumentException("Asset must be associated with a portfolio");
        }
        // Risks can be null or empty, but if present, each risk must have a type
        if (asset.getRisks() != null) {
            asset.getRisks().forEach(risk -> {
                if (risk.getType() == null || risk.getType().isBlank()) {
                    throw new IllegalArgumentException("Each risk associated with an asset must have a type");
                }
            });
        }
        // Add more business-specific validations as needed
    }
}
