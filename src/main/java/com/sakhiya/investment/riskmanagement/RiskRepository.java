package com.sakhiya.investment.riskmanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RiskRepository extends JpaRepository<Risk, UUID> {
    // Find by type
    List<Risk> findByType(String type);

    // Find by asset ID
    List<Risk> findByAsset_AssetId(UUID assetId);

    // Find by calculation date
    List<Risk> findByCalculationDate(LocalDate calculationDate);

    // Find by value greater than or equal to
    List<Risk> findByValueGreaterThanEqual(Double value);

    // Find by confidence level
    List<Risk> findByConfidenceLevel(Double confidenceLevel);

    // Find by scenario
    List<Risk> findByScenario(String scenario);

    // Find by currency
    List<Risk> findByCurrency(String currency);
}
