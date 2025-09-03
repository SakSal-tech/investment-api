package com.sakhiya.investment;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.sakhiya.investment.portfoliomanagement.asset.marketdata.AlphaVantageClient;

@SpringBootApplication
// I had a problem with Springboot not scanning sub packages
@EntityScan(basePackages = "com.sakhiya.investment")
@EnableJpaAuditing

public class InvestmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvestmentApplication.class, args);
		// YProperties is a Java class from the java.util package.
		// It is used to read, write, and manage key-value pairs, typically for
		// configuration files like .properties.

		Properties props = new Properties();
		try (InputStream input = new FileInputStream("src/main/resources/local.properties")) {
			props.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String apiKey = props.getProperty("alphaVantage.apiKey");
		AlphaVantageClient client = new AlphaVantageClient(apiKey);
		try {
			List<Double> prices = client.getDailyPrices("IBM"); // Replace "IBM" with any valid symbol
			System.out.println(prices);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
