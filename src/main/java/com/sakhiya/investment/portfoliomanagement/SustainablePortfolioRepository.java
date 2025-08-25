package com.sakhiya.investment.portfoliomanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SustainablePortfolioRepository extends JpaRepository<SustainablePortfolio, String> {

    // Find by compliance status
    List<SustainablePortfolio> findByComplianceStatus(String complianceStatus);

    // Find by ESG score greater than or equal to a value
    List<SustainablePortfolio> findByOverallEsgScoreGreaterThanEqual(int score);

    // Other queries using Map/List keys are commented out
}
