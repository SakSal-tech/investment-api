# Investment Portfolio Asset & Risk Management

*Empowering sustainable investing with robust risk analysis, asset tracking, and secure client management.*
The **Risk Analysis API** is a backend solution designed to help investors and portfolio managers monitor and evaluate the financial risk exposure of their investments. Built using **Java 17+, Spring Boot, and Spring Data JPA**, this API supports:


[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/your-org/investment/actions)
[![Test Coverage](https://img.shields.io/badge/coverage-95%25-brightgreen)](https://github.com/your-org/investment/actions)
[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)
[![Version](https://img.shields.io/badge/version-1.0.0-blue)](https://github.com/your-org/investment/releases)

--

## Features

- **Client Management:** Each client can have one or more portfolios. Secure user login system with password encoding (use `rawPassword` for login; it will be encoded and hashed automatically).
- **Portfolio Management:** A client can own basic portfolios or sustainable portfolios for greener, environment-friendly investing.
- **Asset Management:** Each portfolio can contain one or more assets. Advanced asset queries by value, name, and risk.
- **Risk Analysis and Summaries:** Includes Value at Risk (VaR) and StressTest calculations for assets, using historic price data.
- **ESG Scoring & Compliance Tracking:** Sustainable portfolios support ESG scoring and compliance status for responsible investing.
- **External API Integration:** Historic stock market data is fetched from external APIs for risk calculations.
- **DTO-based API Responses:** Clean, secure data contracts for all endpoints.
- **Postman & HTTP Files:** Easy API testing for clients and developers.
- **Legacy Support:** Backward compatibility for older portfolio and asset structures.

---

## Table of Contents

- [Description](#description)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Usage](#usage)
  - [For Clients](#for-clients)
  - [For Developers](#for-developers)
- [Database & Dummy Data](#database--dummy-data)
- [UML Diagram](#uml-diagram)
- [Risk Calculations](#risk-calculations)
- [Technologies Used](#technologies-used)
- [Configuration](#configuration)
- [Demo](#demo)
- [Tests](#tests)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgements](#acknowledgements)
- [Contact / Support](#contact--support)
- [FAQ](#faq)
- [Roadmap](#roadmap)
- [Changelog](#changelog)
- [Known Issues](#known-issues)

---

## Description

**Investment Portfolio Asset & Risk Management** is a full-stack Java Spring Boot solution for managing investment portfolios, tracking assets, analyzing risk, and supporting sustainable investing.  
It provides RESTful APIs for clients and developers to interact with portfolios, assets, users, and risk data, with a focus on ESG scoring, compliance, and advanced queries.

- **Purpose:** Help investors and managers build, analyze, and maintain sustainable portfolios.
- **Motivation:** Bridge the gap between traditional asset management and modern ESG/risk requirements.
- **Problem Solved:** Centralizes portfolio, asset, and risk data with powerful APIs and clear separation of concerns.

---

---
Project Structure

Full folder and file structure: docs/PROJECT_STRUCTURE.md


---

## Installation

```bash
# Clone the repository
git clone https://github.com/your-org/investment.git

# Navigate to the project folder
cd investment

# Import dummy data
mysql -u <user> -p <database> < src/main/java/com/sakhiya/investment/data.sql

# Install dependencies (Java, Maven)
mvn clean install

# Run the application
mvn spring-boot:run
```

- **Windows:** Use PowerShell or Command Prompt.
- **Linux/Mac:** Use Terminal.

---

## Usage

### For Clients

- Use the REST API endpoints to manage portfolios, assets, users, and risk data.
- All available endpoints and usage examples are documented in [`user-api.http`](user-api.http).
- For sustainable investing, explore the sustainable portfolio endpoints and ESG scoring features.
- **Note:** A client can have multiple portfolios (basic or sustainable), and each portfolio can have multiple assets.
- **Login:** Use your username and `rawPassword` (plain text) for login; the system will encode and hash your password automatically.

### For Developers

- Import the full [`investment-api.postman_collection`](investment-api.postman_collection) into Postman for all endpoints and payloads.
- Extend DTOs to control data exposure and avoid leaking sensitive fields.
- Add new endpoints in controllers for custom queries.
- Use service classes for business logic and validation.
- Integrate with external APIs for historic price data in risk calculations.
- For further development, you may need to fetch real ESG data for sustainable portfolios from external APIs.
- See [Project Structure](#project-structure) for file organization.

---

## Database & Dummy Data

Ready-to-use dummy data is provided in [`data.sql`](src/main/java/com/sakhiya/investment/data.sql) for quick setup and testing.

- **Instructions:**
  1. Import `data.sql` into your MySQL database before running the application.
  2. This will create sample clients, portfolios, assets, and price history for instant testing.

- **Table Descriptions:**
  - **client:** Stores client details (ID, name, contact, etc.)
  - **portfolio:** Stores portfolio info, links to client, includes ESG and compliance fields for sustainable portfolios.
  - **asset:** Stores asset info, links to portfolio.
  - **asset_price_history:** Stores daily closing prices for assets, used for risk calculations.

- **Notes:**
  - UUIDs are used for primary keys.
  - Asset price history is normally fetched from AlphaVantage API, but manual inserts are provided for testing.
  - Historic prices are set for the last 7 days for testing; developers can extend this for real scenarios.

---

## UML Diagram

A UML diagram is included to illustrate OOP inheritance and relationships between main classes:

- **Client** (can own multiple portfolios)
- **Portfolio** (base class)
  - **SustainablePortfolio** (inherits Portfolio, adds ESG and compliance)
- **Asset** (linked to Portfolio)
- **Risk** (linked to Asset)
- **PriceHistory** (linked to Asset)

![UML Diagram](uml-diagram.png)

This helps visualize how entities are connected and how inheritance is used for portfolio types.

---

## Risk Calculations

- **Value at Risk (VaR):**  
  VaR estimates the maximum loss of an asset or portfolio over a given time horizon at a specified confidence level.  
  It uses historic price data (last 7 days for testing) to calculate risk exposure.  
  Developers can adjust the time window for more realistic scenarios.

- **Stress Testing:**  
  Stress tests simulate extreme market scenarios to assess how assets or portfolios would perform under adverse conditions.

- **Historic Prices:**  
  Asset price history is stored and used for risk calculations.  
  For testing, only the last 7 days are included, but developers can fetch more data from external APIs.

---

## Technologies Used

- Java 17+
- Spring Boot
- JPA/Hibernate
- MySQL
- Maven
- Postman (API testing)
- External APIs for historic stock market data
- [Spring Docs](https://spring.io/projects/spring-boot)

---

## Configuration

- Set DB connection in `application.properties`
- Environment variables for secrets (see `.env.example`)
- API keys for external integrations (if needed)

---

## Demo

- *(Optional: Add a live demo or video link here if available)*

---

## Tests

- **Unit Tests:**  
  Core business logic is tested in service classes, including user authentication, portfolio creation, and asset management.
- **Integration Tests:**  
  Controller endpoints, database interactions, and API flows are verified using real and mock data.
- **Parameterized Tests:**  
  VaR and StressTest risk calculations are tested with multiple input scenarios for accuracy.
- **Mocking:**  
  External API calls (e.g., for historic prices and user authentication) are mocked to ensure reliable test results.
- **Technologies:**  
  Uses JUnit for test structure and assertions, and Mockito for mocking dependencies.
- **How to Run:**  
  ```bash
  mvn test
  ```
- All test results are shown in the terminal or your IDE's test runner.

**Check the `src/test/java/com/sakhiya/investment/` folder for detailed test classes and examples.**

---

## Contributing

- Fork the repo and create a feature branch.
- Follow code style in existing files.
- Submit pull requests with clear descriptions.
- See CONTRIBUTING.md for details.

---

## License

This project is licensed under the MIT License.

---


## FAQ

**Q:** How do I add a new asset type?  
**A:** Extend the Asset entity and update the DTO/controller.

**Q:** How do I run the API locally?  
**A:** See [Installation](#installation).

---

## Roadmap

- Add real-time risk analytics
- Integrate additional external ESG data providers
- Multi-user role support

---

## Changelog

See CHANGELOG.md for version history.

---

## Known Issues

- Some endpoints may return legacy fields; use DTOs for clean responses.
- Data exposure: Always use DTOs in controllers to avoid leaking sensitive or internal fields.
- If you see unexpected data in API responses, check your DTO mappings and controller return types.
- External API failures may affect risk calculations if historic price data is unavailable.
- Legacy support may result in duplicated or deprecated fields in some entities.

---

**Explore the API with [`user-api.http`](user-api.http) and [`investment-api.postman_collection`](investment-api.postman_collection)!**


# Investment Portfolio Asset & Risk Management

*Empowering sustainable investing with robust risk analysis, asset tracking, and secure client management.*

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/your-org/investment/actions)
[![Test Coverage](https://img.shields.io/badge/coverage-95%25-brightgreen)](https://github.com/your-org/investment/actions)
[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)
[![Version](https://img.shields.io/badge/version-1.0.0-blue)](https://github.com/your-org/investment/releases)

---

## Features

- Client and user management with secure login (`rawPassword` encoding).
- Portfolio management (basic & sustainable, ESG scoring).
- Asset management with advanced queries.
- Risk analysis (VaR, StressTest) using historic price data.
- External API integration for market data.
- DTO-based API responses.
- Ready-to-use dummy data and UML diagram in `docs/`.
- Easy API testing via Postman and HTTP files.

---

## Project Structure

See [`docs/PROJECT_STRUCTURE.md`](docs/PROJECT_STRUCTURE.md) for full folder and file details.

---

## Installation

```bash
git clone https://github.com/your-org/investment.git
cd investment
mysql -u <user> -p <database> < src/main/java/com/sakhiya/investment/data.sql
mvn clean install
mvn spring-boot:run
```

---

## Usage

- **Clients:** See [`user-api.http`](user-api.http) for endpoint examples.
- **Developers:** Import [`investment-api.postman_collection`](investment-api.postman_collection) into Postman.
- **Login:** Use your username and `rawPassword` (plain text); password is encoded and hashed automatically.

---

## Database & Dummy Data

- Dummy data setup: see [`data.sql`](src/main/java/com/sakhiya/investment/data.sql).
- Table descriptions and notes: see [`docs/DATABASE.md`](docs/DATABASE.md).

---

## UML Diagram

See [`docs/UML.md`](docs/UML.md) and [`docs/uml-diagram.png`](docs/uml-diagram.png) for class relationships and inheritance.

---

## Risk Calculations

- VaR and StressTest explained in [`docs/RISK.md`](docs/RISK.md).
- Historic prices: last 7 days for testing, extendable via external APIs.

---

## Tests

- Unit, integration, parameterized, and mocking tests.
- See [`src/test/java/com/sakhiya/investment/`](src/test/java/com/sakhiya/investment/) and [`docs/TESTS.md`](docs/TESTS.md).
- Run all tests:
  ```bash
  mvn test
  ```

---

## FAQ, Roadmap, Changelog, Known Issues

See [`docs/FAQ.md`](docs/FAQ.md), [`docs/ROADMAP.md`](docs/ROADMAP.md), [`docs/CHANGELOG.md`](docs/CHANGELOG.md), and [`docs/KNOWN_ISSUES.md`](docs/KNOWN_ISSUES.md).

---

**Explore the API with [`user-api.http`](user-api.http) and [`investment-api.postman_collection`](investment-api.postman_collection)!**