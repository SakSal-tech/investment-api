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
import java.math.BigDecimal;

// Libraries to make createdAt and updatedAt automatically updated whenever any changes happen in this table
import org.springframework.data.annotation.CreatedDate; // Marks a field to be automatically set when entity is first saved
import org.springframework.data.annotation.LastModifiedDate; // Updated automatically on any entity update
import jakarta.persistence.EntityListeners; // Hooks into entity lifecycle events for auditing
import jakarta.persistence.FetchType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener; // Handles populating auditing fields

import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.sakhiya.investment.clientmanagement.Client;
import com.sakhiya.investment.portfoliomanagement.asset.Asset;

@Entity
@EntityListeners(AuditingEntityListener.class) // Enables automatic population of @CreatedDate and @LastModifiedDate
@Table(name = "portfolio")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // All subclasses stored in one table
@DiscriminatorColumn(name = "portfolio_type", discriminatorType = DiscriminatorType.STRING) 
// Adds a column to indicate which subclass a row represents: "EQUITY", "BOND", "MIXED"

public class Portfolio {

    // Auto-generate primary key as UUID stored in CHAR(36) in DB
    @Id
    @Column(name = "portfolioId", columnDefinition = "CHAR(36)")
    private String portfolioId = UUID.randomUUID().toString(); 
    // Generates a 36-char string that maps to CHAR(36) in MySQL
    // Fixes issues with JPA/Hibernate UUID mapping and Postman display

    private String portfolioName; // Optional descriptive name
    
    @ManyToOne // one client has one or more portfolio
    @JoinColumn(name = "client_id", columnDefinition = "CHAR(36)") // joining clientId which is PK in Client table here as FK
    private Client client; // storing the relationship as an object
    //private String clientId; // Foreign key to Client entity

    @CreatedDate
    private LocalDate createdAt; // Creation date, auto-set on first save

    @LastModifiedDate
    private LocalDate updatedAt; // Auto-updated whenever entity is updated

    private String investmentGoal; // Growth, Income, Capital Preservation
    private Integer riskLevel; // 1-5 scale

    private BigDecimal totalValue; // Optional, can be computed

    // Aggregated numeric total of all VaR calculations across all assets
    // Useful for quick reporting, dashboards, historical tracking without recalculating every time
        @jakarta.persistence.PrePersist
        protected void onCreate() {
            if (this.createdAt == null) {
                this.createdAt = java.time.LocalDate.now();
            }
            if (this.updatedAt == null) {
                this.updatedAt = java.time.LocalDate.now();
            }
        }
    private Double totalVaR;

    // Aggregated numeric total of all Stress Test calculations across all assets
    // Provides an overall view of portfolio exposure under stress scenarios
    private Double totalStressTest;

    // This is the list of assets linked to this portfolio
    @OneToMany(mappedBy = "portfolio", fetch = FetchType.EAGER) // One portfolio can have many assets
    private List<Asset> assets;// List of all assets associated with this portfolio



    // Default no-arg constructor required by JPA
    public Portfolio() {
    }

    // Constructor with all fields
    public Portfolio(String portfolioName, Client client, LocalDate createdAt, LocalDate updatedAt,
            String investmentGoal, Integer riskLevel, BigDecimal totalValue, List<Asset> assets) {
        this.portfolioName = portfolioName;
        this.client = client;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.investmentGoal = investmentGoal;
        this.riskLevel = riskLevel;
        this.totalValue = totalValue;
        this.assets = assets;
    }

    // Getter/setter for portfolioId
    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    // Getter/setter for portfolioName
    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    // Getter/setter for clientId
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // Getter/setter for createdAt
    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt != null ? createdAt : LocalDate.now(); 
        // Defaults to current date if null
    }

    // Getter/setter for updatedAt
    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt != null ? updatedAt : LocalDate.now(); 
        // Defaults to current date if null
    }

    // Getter/setter for totalValue
    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    // Getter/setter for investmentGoal
    public String getInvestmentGoal() {
        return investmentGoal;
    }

    public void setInvestmentGoal(String investmentGoal) {
        this.investmentGoal = investmentGoal;
    }

    // Getter/setter for riskLevel
    public Integer getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Integer riskLevel) {
        this.riskLevel = riskLevel;
    }

    // Getter/setter for assets
    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    // Getter/setter for totalVaR
    public Double getTotalVaR() {
        return totalVaR;
    }

    public void setTotalVaR(Double totalVaR) {
        this.totalVaR = totalVaR;
    }

    // Getter/setter for totalStressTest
    public Double getTotalStressTest() {
        return totalStressTest;
    }

    public void setTotalStressTest(Double totalStressTest) {
        this.totalStressTest = totalStressTest;
    }
}
