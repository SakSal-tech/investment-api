package com.sakhiya.investment.portfoliomanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface SustainablePortfolioRepository extends JpaRepository<SustainablePortfolio, String> {
    // Find by compliance status
    List<SustainablePortfolio> findByComplianceStatus(String complianceStatus);

    // Find by theme focus (themes like 'climate', 'human rights')
    List<SustainablePortfolio> findByThemeFocusContaining(String theme);

    // Find by excluded sector
    List<SustainablePortfolio> findByExcludedSectorsContaining(String sector);

    // Find by preferred sector
    List<SustainablePortfolio> findByPreferredSectorsContaining(String sector);

    // Find by ESG score greater than or equal to a value
    List<SustainablePortfolio> findByOverallEsgScoreGreaterThanEqual(int score);

    // Find by last updated after a certain date
    List<SustainablePortfolio> findByLastUpdatedAfter(java.time.LocalDate date);

    // I changed from Spring Data method names to JPQL queries below because Spring Data JPA
    // does not support derived queries on Map keys/values. Using @Query with JPQL allows me
    // to search for entries in a Map field (annotated with @ElementCollection) by key.

    // Find by impact target key using JPQL (for Map<String, String> impactTargets)
    //   JOIN s.impactTargets i: This joins the impactTargets map, so 'i' is each map entry (key-value pair) for each portfolio.
    //   KEY(i) = :key: 'KEY(i)' gets the key from the map entry. ':key' is a named parameter that will be replaced by the method argument at runtime.
    @Query("SELECT s FROM SustainablePortfolio s JOIN s.impactTargets i WHERE KEY(i) = :key")
    List<SustainablePortfolio> findByImpactTargetKey(@Param("key") String key);

    // Find by ESG score key using JPQL (for Map<String, Integer> esgScores)
    // JPQL explanation:
    //   JOIN s.esgScores e: Joins the esgScores map, so 'e' is each map entry.
    //   KEY(e) = :key: Filters for map entries where the key matches the provided parameter.
    @Query("SELECT s FROM SustainablePortfolio s JOIN s.esgScores e WHERE KEY(e) = :key")
    List<SustainablePortfolio> findByEsgScoreKey(@Param("key") String key);
}
