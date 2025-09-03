package com.sakhiya.investment.portfoliomanagement.asset;

import com.sakhiya.investment.riskmanagement.dto.RiskSummaryDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
/*
 * AssetService class is a Spring Boot Service Layer for managing Assets in this API.
   It acts as an intermediary between the Controller (API endpoints) and the Repository (database),
 */
public class AssetService {
    private final AssetRepository assetRepository;

    // Dependencies: AssetRepository is injected via constructor injection
    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    // Returns all assets from the database. Uses Spring Data JPA’s built-in findAll() method.
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    // Returns an asset wrapped in Optional if it exists by its unique UUID, or empty if not found. Returns an asset .
    // Optional for handling null values safely.
    public Optional<Asset> getAssetById(String id) {
        return assetRepository.findById(id);
    }

    // Validates calling validateAsset method and creates a new asset using built in save method
    public Asset createAsset(Asset asset) {
        validateAsset(asset);
        return assetRepository.save(asset);
    }

    // Validates and updates an existing asset only certain fields (name, value,  portfolio, risks).
    // Returns Optional<Asset> and empty if ID not found.
    public Optional<Asset> updateAsset(String id, Asset updatedAsset) {
        validateAsset(updatedAsset);
        // If the Optional is empty,the lambda never runs .map() just returns an empty Optional
        return assetRepository.findById(id).map(existingAsset -> {
            //The lambda returns the saved asset, which becomes the new value inside the Optional
            // Only update fields that are allowed to change
            existingAsset.setName(updatedAsset.getName());
            existingAsset.setValue(updatedAsset.getValue());
            existingAsset.setPortfolio(updatedAsset.getPortfolio());
            existingAsset.setRisks(updatedAsset.getRisks());

            return assetRepository.save(existingAsset);
        });
    }

    // Deletes an asset by its UUID. Deletes asset if it exists. Returns true if deletion was successful
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

    // Following the DRY (Don't Repeat Yourself), I put vaidations in one method
    // instead of checking if ID exists in different methods.
    // Validates the asset object for required fields and business rules. Ensures:
    // Asset name is required. Value is ≥ 0.
    // Portfolio is required. Each Risk has a type.
    private void validateAsset(Asset asset) {
        // Asset name must not be null or blank, as it's a key identifier for users
        if (asset.getName() == null || asset.getName().isBlank()) {
            throw new IllegalArgumentException("Asset name cannot be blank");
        }
        // Asset value must be non-negative, as negative asset values don't make sense
        if (asset.getValue() == null || asset.getValue() < 0) {
            throw new IllegalArgumentException("Asset value must be non-negative");
        }
        // Portfolio must not be null, as every asset should belong to a portfolio in
        // this system
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
    }

    // Retrieves an asset’s risks of a specific type and sums their values. Returns
    // the total value of risks of a given type (e.g., "VaR" or "StressTest") for a
    // specific asset
    public double getTotalRiskValueByType(String assetId, String riskType) {
        Optional<Asset> assetOpt = assetRepository.findById(assetId);// fetch asset by id from database
        if (assetOpt.isEmpty()) {
            throw new IllegalArgumentException("Asset with id " + assetId + " not found");// If no asset exists with
                                                                                          // that ID, an exception is
                                                                                          // thrown.
        }
        Asset asset = assetOpt.get();// Retrieves the Asset object from Optional
        if (asset.getRisks() == null)
            return 0.0;// If the asset has no risks list, the method returns 0.0.
        return asset.getRisks().stream()// turning the list of risks into a processing pipeline to filter
                // declare risk as an object of Risk class from List<Risk> . Only risks of risk
                // type requested, ignoring null values
                .filter(risk -> riskType.equalsIgnoreCase(risk.getType()) && risk.getValue() != null)// ignore case e.g
                                                                                                     // VAR or var
                // Convert each Risk into its numeric value. mapToDouble(...) is a method in the
                // Stream API that converts Stream<T> into a DoubleStream.
                // DoubleStream is a specialized stream type for primitive doubles. sum method does not work on Double wrapper class
                .mapToDouble(risk -> risk.getValue())
                .sum();

        // Same logic as above but more verbose and no stream is used. I had to change it to above using lambda for efficient way to get from a Stream<Risk> to a sum of doubles.
        /*
         * double sum = 0.0;
         * for (Risk risk : asset.getRisks()) {
         * if (riskType.equalsIgnoreCase(risk.getType()) && risk.getValue() != null) {
         * sum += risk.getValue();
         * }
         * }
         * return sum;
         */

    }

        // Returns a RiskSummaryDTO with total VaR and total StressTest for a given asset
    public RiskSummaryDTO getTotalRiskSummary(String assetId) {
        // Try to find the asset by its ID in the database
        Optional<Asset> assetOpt = assetRepository.findById(assetId);
        // If the asset does not exist, throw an error with a helpful message
        if (assetOpt.isEmpty()) {
            throw new IllegalArgumentException("Asset with id " + assetId + " not found");
        }
        // Get the actual Asset object from the Optional
        Asset asset = assetOpt.get();
        // If the asset has no risks at all, return a summary with both values as 0
        if (asset.getRisks() == null) {
            return new RiskSummaryDTO(0.0, 0.0);
        }
        // Calculate the total VaR (Value at Risk) by summing all risk values of type "VaR"
        double totalVaR = asset.getRisks().stream()
            // Only include risks where the type is "VaR" (case-insensitive) and the value is not null
            .filter(risk -> "VaR".equalsIgnoreCase(risk.getType()) && risk.getValue() != null)
            // Convert each matching risk to its numeric value
            .mapToDouble(risk -> risk.getValue())
            // Add up all the values
            .sum();
        // Calculate the total StressTest by summing all risk values of type "StressTest"
        double totalStressTest = asset.getRisks().stream()
            // Only include risks where the type is "StressTest" (case-insensitive) and the value is not null
            .filter(risk -> "StressTest".equalsIgnoreCase(risk.getType()) && risk.getValue() != null)
            // Convert each matching risk to its numeric value
            .mapToDouble(risk -> risk.getValue())
            // Add up all the values
            .sum();
        // Return a summary object containing both totals
        return new RiskSummaryDTO(totalVaR, totalStressTest);
    }


}
