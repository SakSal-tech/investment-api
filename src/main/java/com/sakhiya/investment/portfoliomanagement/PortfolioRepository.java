package com.sakhiya.investment.portfoliomanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;

@Repository
/*changed from ListCrudRepository to JpaRepository for advanced features (paging, sorting, batch),  JpaRepository<Portfolio, String>.
 basic CRUD, ListCrudRepository<Portfolio, String> is fine. The ID type (String or UUID) must match your entity’s ID field.*/

public interface PortfolioRepository extends JpaRepository<Portfolio, String> {
    // Find all portfolios for a specific client
    List<Portfolio> findByClientId(String clientId);

    // Find portfolios by investment goal
    List<Portfolio> findByInvestmentGoal(String investmentGoal);

    // Find portfolios by risk level
    List<Portfolio> findByRiskLevel(Integer riskLevel);

    // Find portfolios created after a certain date
    List<Portfolio> findByCreatedAtAfter(LocalDate date);

    // Find portfolios with total value greater than a certain amount
    List<Portfolio> findByTotalValueGreaterThan(BigDecimal value);

    // Find portfolios by total VaR greater than a value
    List<Portfolio> findByTotalVaRGreaterThan(Double totalVaR);

    // Find portfolios by total Stress Test greater than a value
    List<Portfolio> findByTotalStressTestGreaterThan(Double totalStressTest);
}
