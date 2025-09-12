## Project Structure

This document outlines the main folders and files in the Investment Portfolio Asset & Risk Management API project.

## Root Directory

- `README.md` — Project overview and documentation
- `LICENSE` — License information
- `pom.xml` — Maven build configuration
- `swagger.yaml` — OpenAPI specification
- `mvnw`, `mvnw.cmd` — Maven wrapper scripts

## docs/

- `data.sql` — Sample database data for setup/testing
- `DATABASE.md` — Database table descriptions
- `entity-relational-diagram.png` — Entity relationship diagram
- `ERRORS.md` — Error codes and messages
- `HELP.md` — Help and usage notes
- `investment_dump.sql` — Full database backup
- `investment-api-full.postman_collection.json` — Postman collection for API testing
- `KNOWN_ISSUES.md` — Known issues and limitations
- `Risk.md` — Risk calculation documentation
- `Roadmap.md` — Planned features and enhancements
- `Tests.md` — Test documentation
- `TODO.md` — To-do list and planned tasks
- `UMLDiagram.png` — UML class diagram
- `useful-learning-links.md` — Recommended resources
- `user-api.http` — HTTP file for API testing

## src/main/java/com/sakhiya/investment/

- `InvestmentApplication.java` — Main Spring Boot application
- `SecurityConfig.java` — Spring Security configuration
- `clientmanagement/` — Client and user management code
- `portfoliomanagement/` — Portfolio and asset management code
- `riskmanagement/` — Risk analysis and calculation code
- `util/` — Utility classes (validation, helpers)
- `config/` — Additional configuration classes

## src/main/resources/

- `application.properties` — Main application configuration
- `local.properties` — Local environment configuration

## src/test/java/com/sakhiya/investment/

- Unit, integration, and parameterized tests for all modules

## target/

- Compiled classes, build artifacts, and generated sources

---

# Packages and Code file Structure

src/
├── main/
│ └── java/
│ └── com/
│ └── sakhiya/
│ └── investment/
│ ├── clientmanagement/
│ │ ├── Client.java
│ │ ├── ClientController.java
│ │ ├── ClientRepository.java
│ │ ├── ClientService.java
│ │ ├── User.java
│ │ ├── UserController.java
│ │ ├── UserDTO.java
│ │ ├── UserRepository.java
│ │ ├── UserService.java
│ ├── portfoliomanagement/
│ │ ├── Portfolio.java
│ │ ├── PortfolioController.java
│ │ ├── PortfolioCreateDTO.java
│ │ ├── PortfolioDTO.java
│ │ ├── PortfolioRepository.java
│ │ ├── PortfolioService.java
│ │ ├── SustainablePortfolio.java
│ │ ├── SustainablePortfolioController.java
│ │ ├── SustainablePortfolioCreateDTO.java
│ │ ├── SustainablePortfolioDTO.java
│ │ ├── SustainablePortfolioRepository.java
│ │ ├── SustainablePortfolioService.java
│ │ ├── asset/
│ │ │ ├── Asset.java
│ │ │ ├── AssetController.java
│ │ │ ├── AssetHistoryService.java
│ │ │ ├── AssetPriceHistory.java
│ │ │ ├── AssetPriceHistoryController.java
│ │ │ ├── AssetPriceHistoryDTO.java
│ │ │ ├── AssetPriceHistoryRepository.java
│ │ │ ├── AssetRepository.java
│ │ │ ├── AssetService.java
│ │ │ ├── marketdata/
│ │ │ │ ├── AlphaVantageClient.java
│ │ │ │ ├── AlphaVantagePriceDTO.java
│ ├── riskmanagement/
│ │ ├── Risk.java
│ │ ├── RiskController.java
│ │ ├── RiskRepository.java
│ │ ├── RiskService.java
│ │ ├── dto/
│ │ │ ├── RiskSummaryDTO.java
│ │ │ ├── VaRCalculationDetailsDTO.java
│ ├── util/
│ │ ├── Validations.java
│ ├── config/
│ │ ├── PasswordEncoderConfig.java
│ ├── SecurityConfig.java
│ ├── InvestmentApplication.java
