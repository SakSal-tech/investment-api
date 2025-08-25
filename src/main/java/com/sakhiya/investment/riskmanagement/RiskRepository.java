package com.sakhiya.investment.riskmanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RiskRepository extends JpaRepository<Risk, String> {
    // Find by type
    List<Risk> findByType(String type);

    // Find by asset ID. returns a list of multiple Risk records associated with a single asset
    List<Risk> findByAsset_AssetId(String assetId);

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

    // Find the first Risk with type "VaR" from a list
    Optional<Risk> findFirstByAsset_AssetIdAndType(String assetId, String type);
}
