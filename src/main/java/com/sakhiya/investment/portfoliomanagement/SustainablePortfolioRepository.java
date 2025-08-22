package com.sakhiya.investment.portfoliomanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
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

    // Find by impact target key
    List<SustainablePortfolio> findByImpactTargetsKey(String key);

    // Find by ESG score for a specific holding (key)
    List<SustainablePortfolio> findByEsgScoresKey(String key);
}
