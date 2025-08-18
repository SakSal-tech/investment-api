package com.sakhiya.investment.portfoliomanagement.asset;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.UUID;
import com.sakhiya.investment.portfoliomanagement.Portfolio;

@Entity
public class Asset {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID assetId;

	private String name;
	private Double value;

	@ManyToOne
	@JoinColumn(name = "portfolio_id")
	private Portfolio portfolio;

	public Asset() {}

	public Asset(String name, Double value, Portfolio portfolio) {
		this.name = name;
		this.value = value;
		this.portfolio = portfolio;
	}

	public UUID getAssetId() {
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
}
