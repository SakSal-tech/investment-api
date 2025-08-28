package com.sakhiya.investment.portfoliomanagement.asset;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sakhiya.investment.portfoliomanagement.Portfolio;
import com.sakhiya.investment.riskmanagement.Risk;

@Entity
public class Asset {

	@Id
    @Column(name = "asset_Id", columnDefinition = "CHAR(36)")
    private String assetId = UUID.randomUUID().toString();  // store UUID as String
    //generates a proper 36-char string that maps to CHAR(36) in MySQL. 
    //I had issues with the way UUID is stored and the way is presented by postman problem how with JPA and Hibernate was mapping it 


	private String name;
	@Column(name = "`value`")
	private Double value;

	@ManyToOne
	@JoinColumn(name = "portfolio_id")
	private Portfolio portfolio;
	// To prevent JSON recursion Cyclic references (Portfolio → Asset → Risk → Asset):This can cause issues with JSON serialization (infinite recursion) and sometimes with JPA if not handled
	@JsonManagedReference 
	@JsonBackReference

	// one Asset can have many Risks.Cascade” means changes to Asset will also apply
	// to its Risks automatically.
	// If an Asset is removed from the risks list, JPA will automatically delete
	// that Risk from the database.
	// Prevents “orphaned” risks that no longer belong to any asset

	@OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Risk> risks;

	public Asset() {
	}

	public Asset(String name, Double value, Portfolio portfolio) {
		this.name = name;
		this.value = value;
		this.portfolio = portfolio;
	}

	public String getAssetId() {
		return assetId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public List<Risk> getRisks() {
		return risks;
	}

	public void setRisks(List<Risk> risks) {
		this.risks = risks;
	}

}
