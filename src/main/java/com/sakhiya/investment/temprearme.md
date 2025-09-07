# Risk Analysis API

*Assess and manage financial risk across investment portfolios with a robust Java Spring Boot backend.*

The **Risk Analysis API** is a backend solution designed to help investors and portfolio managers monitor and evaluate the financial risk exposure of their investments. Built using **Java 17+, Spring Boot, and Spring Data JPA**, this API supports:

- Full **portfolio CRUD operations**: create, read, update, and delete portfolios.
- **Asset management** within portfolios, including Stocks, Bonds, and Funds, using inheritance from a common Asset base class.
- **Advanced risk calculations**, including:
  - **Value at Risk (VaR)** using the Historical Method
  - **Stress Testing** for predefined scenarios such as Market Crashes or Interest Rate Spikes
- **Structured JSON responses** for all endpoints and clear error messages for invalid requests.
- Ready-to-use **unit tests** for core services and risk calculations.

> Designed to bridge traditional portfolio management and modern risk analytics, providing developers with clean, RESTful endpoints and detailed documentation for rapid integration and testing.

---

## Features

- Portfolio & Asset CRUD operations
- Asset filtering by type, risk level, and sector/category
- Risk analysis (VaR & Stress Testing)
- Structured JSON error responses
- Unit tests with at least 80% coverage
- RESTful API design with Controller-Service-Repository architecture

> Full feature details: [docs/FEATURES.md](docs/FEATURES.md)

---

## Installation

```bash
git clone https://github.com/your-username/risk-analysis-api.git
cd risk-analysis-api
mysql -u <user> -p <database> < src/main/java/com/yourname/investment/data.sql
mvn clean install
mvn spring-boot:run
