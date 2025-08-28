package com.sakhiya.investment.portfoliomanagement.asset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
//extends JpaRepository<Asset, String>, providing CRUD and custom query methods
public interface AssetRepository extends JpaRepository<Asset, String> {

    // Find assets by name
    List<Asset> findByName(String name);

    // Find assets by value greater than or equal to
    List<Asset> findByValueGreaterThanEqual(Double value);

    // Find assets by value less than or equal to
    List<Asset> findByValueLessThanEqual(Double value);

    // Find assets by value between two values
    List<Asset> findByValueBetween(Double min, Double max);

    // Find assets by portfolio ID
    List<Asset> findByPortfolio_PortfolioId(String portfolioId);

    // Find assets by name containing substring (case-insensitive)
    List<Asset> findByNameContainingIgnoreCase(String substring);

    // Find assets by exact value
    List<Asset> findByValue(Double value);

    // Count assets by portfolio ID
    //will return the total number of Asset records that belong to a specific portfolio.
    long countByPortfolio_PortfolioId(String portfolioId);

    // Find assets that have a specific risk by risk type
    List<Asset> findByRisks_Type(String type);

    // Find assets that have any risks
    List<Asset> findByRisksIsNotNull();

    // Find assets with no risks
    List<Asset> findByRisksIsNull();
}
