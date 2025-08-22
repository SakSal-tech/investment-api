
package com.sakhiya.investment.portfoliomanagement;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

// I need these libraries to make the created date and updated date automaticlly update whenever any changes happens in any fields  in this table
import org.springframework.data.annotation.CreatedDate;//Marks a field in entity to be automatically set with the creation timestamp when the entity is first saved.
import org.springframework.data.annotation.LastModifiedDate;//Every time the entity is updated through the repository, Spring Data updates this field automatically.
import jakarta.persistence.EntityListeners; //The listener class that implements the logic for auditing. It hooks into the entity lifecycle events and automatically sets the annotated fields (createdAt, updatedAt) with timestamps
import org.springframework.data.jpa.domain.support.AuditingEntityListener;//handles populating the fields.
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.InheritanceType;
import com.sakhiya.investment.portfoliomanagement.asset.Asset;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "portfolio")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // All subclasses are stored in one database table. Tells JPA how
                                                      // to map a class hierarchy (parent + subclasses) to database
                                                      // tables
@DiscriminatorColumn(name = "portfolio_type", discriminatorType = DiscriminatorType.STRING) // distinguish. Adds a
                                                                                            // special column to the
                                                                                            // table to indicate which
                                                                                            // subclass a row represents
                                                                                            // "EQUITY", "BOND",
                                                                                            // "MIXED".

public class Portfolio {

    // auto generate primary key
    @Id
    @Column(name = "portfolioId", columnDefinition = "CHAR(36)")
    private String portfolioId = UUID.randomUUID().toString(); // store UUID as String
    // generates a proper 36-char string that maps to CHAR(36) in MySQL.
    // I had issues with the way UUID is stored and the way is presented by postman
    // problem how with JPA and Hibernate was mapping it

    private String portfolioName; // optional descriptive name
    private String clientId; // foreign key to Client entity
    @CreatedDate
    private LocalDate createdAt; // creation timestamp
    @LastModifiedDate
    private LocalDate updatedAt; // last update timestamp
    private String investmentGoal; // Growth, Income, Capital Preservation
    private Integer riskLevel; // 1-5 scale

    private BigDecimal totalValue; // optional, can be computed
    // Aggregated numeric total of all VaR (Value at Risk) calculations across all
    // assets in this portfolio.
    // Useful for quick reporting, dashboards, and historical tracking without
    // recalculating every time.
    private Double totalVaR;

    // Aggregated numeric total of all Stress Test calculations across all assets in
    // this portfolio.
    // Provides an overall view of portfolio exposure under various stress
    // scenarios.
    private Double totalStressTest;

    @OneToMany(mappedBy = "portfolio") // one portfolio could have many assets
    private List<Asset> assets;// List of all assets associated to this portfolio

    public Portfolio() {
    }

    public Portfolio(String portfolioName, String clientId, LocalDate createdAt, LocalDate updatedAt,
            String investmentGoal, Integer riskLevel, BigDecimal totalValue, List<Asset> assets) {
        this.portfolioName = portfolioName;
        this.clientId = clientId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.investmentGoal = investmentGoal;
        this.riskLevel = riskLevel;
        this.totalValue = totalValue;
        this.assets = assets;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clienttId) {
        this.clientId = clienttId;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = LocalDate.now();// date portfolio created which is current date
        ;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = LocalDate.now();// initially same as date created unless it is updated.
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public String getInvestmentGoal() {
        return investmentGoal;
    }

    public void setInvestmentGoal(String investmentGoal) {
        this.investmentGoal = investmentGoal;
    }

    public Integer getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    // Getter for total VaR
    public Double getTotalVaR() {
        return totalVaR;
    }

    // Setter for total VaR
    public void setTotalVaR(Double totalVaR) {
        this.totalVaR = totalVaR;
    }

    // Getter for total Stress Test
    public Double getTotalStressTest() {
        return totalStressTest;
    }

    // Setter for total Stress Test
    public void setTotalStressTest(Double totalStressTest) {
        this.totalStressTest = totalStressTest;
    }

}
