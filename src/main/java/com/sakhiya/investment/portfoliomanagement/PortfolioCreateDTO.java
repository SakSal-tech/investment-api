package com.sakhiya.investment.portfoliomanagement;

// PortfolioCreateDTO.java separates your API contract from your database model, making your code more flexible and secure.
//This DTO's fields to not exactly match the entity's fields. 
//A DTO (Data Transfer Object) is designed for how data is sent/received over the network, not how it's stored in the database.
//The DTO is optimised for API clients (e.g., it uses clientId instead of a full Client object). The entity is optimised for JPA/Hibernate and database relationships (e.g., it uses a Client object, not just an ID).
//Some fields (like audit fields, internal IDs, or calculated values) may not be needed in the Post request
public class PortfolioCreateDTO {
    public String portfolioName;
    public String clientId;
    public String investmentGoal;
    public Integer riskLevel;
    public java.math.BigDecimal totalValue;
    public String createdAt;
    public String updatedAt;
}