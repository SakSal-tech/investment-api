package com.sakhiya.investment.riskmanagement;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "risk")

public class Risk {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // Unique identifier for the risk

    private String type; // e.g., "VaR", "StressTest", "CreditRisk"
    private String description; // Details about the risk
    private Double value; // Calculated risk value (e.g., VaR amount)

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private com.sakhiya.investment.portfoliomanagement.asset.Asset asset; // The asset this risk is associated with

    // Constructors
    public Risk() {}

    public Risk(String type, String description, Double value, com.sakhiya.investment.portfoliomanagement.asset.Asset asset) {
        this.type = type;
        this.description = description;
        this.value = value;
        this.asset = asset;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
    // alternative to importing the class
    public com.sakhiya.investment.portfoliomanagement.asset.Asset getAsset() { return asset; }
    public void setAsset(com.sakhiya.investment.portfoliomanagement.asset.Asset asset) { this.asset = asset; }
}