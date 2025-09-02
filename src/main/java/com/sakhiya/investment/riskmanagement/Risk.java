package com.sakhiya.investment.riskmanagement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sakhiya.investment.portfoliomanagement.asset.Asset;// need to use asset to link risks to assets
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
@Entity
@Table(name = "risk")
public class Risk

{

    @Id
    @Column(name = "risk_id", columnDefinition = "CHAR(36)")
    private String riskId = UUID.randomUUID().toString();  // store UUID as String
    //generates a proper 36-char string that maps to CHAR(36) in MySQL. 
    //I had issues with the way UUID is stored and the way is presented by postman problem how with JPA and Hibernate was mapping it 


    private String type; // "VaR", "StressTest", etc. Used to distinguish risk type
    private String description; // Short description of the risk
    @Column(name = "`value`")
    private Double value; // Main numeric outcome for aggregation/reporting
    private LocalDate calculationDate; // When the risk was calculated
    private Double confidenceLevel; // Relevant for VaR (e.g., 95% or 99%)
    private String timeHorizon; // Relevant for VaR (e.g., "1 day", "10 days")
    private String scenario; // Relevant for StressTest
    private String currency; // Currency of the value
    //intended to store complex results, like:Full VaR calculations with multiple confidence intervals, Stress Test scenarios and Historical or detailed analytics in JSON format
    @Lob //Stands for: Large Object. Tells JPA that this field may contain a large amount of data, which wouldn't fit in a standard column like VARCHAR(255).
    private String detailsJson; // Store full detailed calculation results (JSON for analytics/export)

    @ManyToOne
    @JoinColumn(name = "asset_id", columnDefinition = "CHAR(36)")
    // @JsonBackReference is used here to prevent infinite recursion during JSON serialization.
    // In a bidirectional relationship, Asset has a list of Risks (@JsonManagedReference),
    // and each Risk points back to its Asset. Jackson uses these annotations to serialize only one direction,
    // avoiding stack overflow errors when converting to/from JSON.
    // When converting (serialize) a Java object to JSON, it is turning  Java objects ( Asset and Risk) into a JSON string that can be sent over HTTP or stored. When you convert (deserialize) from JSON, you are turning a JSON string (from a request or file) back into Java objects
    @JsonBackReference
    private Asset asset; // The asset this risk is associated with

    // Constructors
    public Risk() { }

    public Risk(String type, String description, Double value,
                com.sakhiya.investment.portfoliomanagement.asset.Asset asset)
    {
        this.type = type;
        this.description = description;
        this.value = value;
        this.asset = asset;
    }

    // Getters and Setters
    public String getId()
    {
        return riskId;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Double getValue()
    {
        return value;
    }

    public void setValue(Double value)
    {
        this.value = value;
    }

    public LocalDate getCalculationDate()
    {
        return calculationDate;
    }

    public void setCalculationDate(LocalDate calculationDate)
    {
        this.calculationDate = calculationDate;
    }

    public Double getConfidenceLevel()
    {
        return confidenceLevel;
    }

    public void setConfidenceLevel(Double confidenceLevel)
    {
        this.confidenceLevel = confidenceLevel;
    }

    public String getTimeHorizon()
    {
        return timeHorizon;
    }

    public void setTimeHorizon(String timeHorizon)
    {
        this.timeHorizon = timeHorizon;
    }

    public String getScenario()
    {
        return scenario;
    }

    public void setScenario(String scenario)
    {
        this.scenario = scenario;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getDetailsJson()
    {
        return detailsJson;
    }

    public void setDetailsJson(String detailsJson)
    {
        this.detailsJson = detailsJson;
    }

    public com.sakhiya.investment.portfoliomanagement.asset.Asset getAsset()
    {
        return asset;
    }

    public void setAsset(com.sakhiya.investment.portfoliomanagement.asset.Asset asset)
    {
        this.asset = asset;
    }


}
