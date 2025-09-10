# Investment Portfolio Asset & Risk Management

*Empowering sustainable investing with robust risk analysis, asset tracking, and secure client management.*

The **Risk Analysis API** is a backend solution designed to help investors and portfolio managers monitor and evaluate the financial risk exposure of their investments. Built using **Java 17+, Spring Boot, and Spring Data JPA**, this API supports:

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
- [UML Diagram](#uml-diagram)
- [Entity Relational Diagram](#entity-relational-diagram)
- [Installation](#installation)
- [Usage](#usage)
  - [For Clients](#for-clients)
  - [For Developers](#for-developers)
- [Database & Dummy Data](#database--dummy-data)
- [Risk Calculations](#risk-calculations)
- [Technologies Used](#technologies-used)
- [Tests](#tests)
- [Contributing](#contributing)
- [License](#license)
- [FAQ](#faq)
- [Roadmap](#roadmap)
- [Known Issues](#known-issues)
- [API Documentation (Swagger)](#api-documentation-swagger)
- [Configuration](#configuration)
- [Validation Utility](#validation-utility)
- [Technical Notes: JSON Data Storage](#technical-notes-json-data-storage)
- [Useful Learning Links](#useful-learning-links)
- [To Do List](#to-do-list)

---

## Description

**Investment Portfolio Asset & Risk Management** is a full-stack Java Spring Boot solution for managing investment portfolios, tracking assets, analyzing risk, and supporting sustainable investing.  
It provides RESTful APIs for clients and developers to interact with portfolios, assets, users, and risk data, with a focus on ESG scoring, compliance, and advanced queries.

- **Purpose:** Help investors and managers build, analyze, and maintain sustainable portfolios.
- **Motivation:** Bridge the gap between traditional asset management and modern ESG/risk requirements.
- **Problem Solved:** Centralizes portfolio, asset, and risk data with powerful APIs and clear separation of concerns.

---

## Project Structure

See [`docs/PROJECT_STRUCTURE.md`](docs/PROJECT_STRUCTURE.md) for full folder and file details.

---

## UML Diagram

See [`docs/UML.md`](docs/UML.md) and [`docs/uml-diagram.png`](docs/uml-diagram.png) for class relationships and inheritance.

---

## Entity Relational Diagram

See [`docs/entity-relational-diagram.png`](docs/entity-relational-diagram.png) for a visual overview of all database entities and their relationships.

---



## Installation

```bash
git clone https://github.com/SakSal-tech/investment-api
cd investment
mysql -u <user> -p <database> < docs/data.sql
mvn clean install
mvn spring-boot:run
```

- **Windows:** Use PowerShell or Command Prompt.
- **Linux/Mac:** Use Terminal.

---

## Usage

### Database Backup
A full database backup is available in [`docs/investment_dump.sql`](docs/investment_dump.sql) for restoring or migrating your investment data.

### For Clients

- Use the REST API endpoints to manage portfolios, assets, users, and risk data.
- All available endpoints and usage examples are documented in [`docs/user-api.http`](docs/user-api.http).
- For sustainable investing, explore the sustainable portfolio endpoints and ESG scoring features.
- **Note:** A client can have multiple portfolios (basic or sustainable), and each portfolio can have multiple assets.
- **Login:** Use username and `rawPassword` (plain text) for login; the system will encode and hash  password automatically.

### For Developers

- Import the full [`docs/investment-api-full.postman_collection.json`](docs/investment-api-full.postman_collection.json) into Postman for all endpoints and payloads.
- Extend DTOs to control data exposure and avoid leaking sensitive fields.
- Add new endpoints in controllers for custom queries.
- Use service classes for business logic and validation.
- Integrate with external APIs for historic price data in risk calculations.
- For further development, you may need to fetch real ESG data for sustainable portfolios from external APIs.
- See [Project Structure](#project-structure) for file organization.
> **Note:**  
> All API endpoints that use path variables (such as IDs, usernames, etc.) must use curly braces `{}` in the URL path.  
> For example: `/api/clients/{clientId}` or `/api/users/{username}`.  
> Make sure your Postman collection and any API clients use this format to match the Spring Boot controller mappings.

---

## Database & Dummy Data

Ready-to-use dummy data is provided in [`docs/data.sql`](docs/data.sql) for quick setup and testing.

- **Instructions:**
  1. Import `data.sql` into your MySQL database before running the application.
  2. This will create sample clients, portfolios, assets, and price history for instant testing.

- **Table Descriptions:** See [`docs/DATABASE.md`](docs/DATABASE.md).

---

## Risk Calculations

- VaR and StressTest explained in [`docs/RISK.md`](docs/RISK.md).
- Historic prices: last 7 days for testing, extendable via external APIs.

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

## Tests

- Unit, integration, parameterized, and mocking tests.
- See [`src/test/java/com/sakhiya/investment/`](src/test/java/com/sakhiya/investment/) and [`docs/TESTS.md`](docs/TESTS.md).
- Run all tests:
  ```bash
  mvn test
  ```

---

## Contributing

- Fork the repo and create a feature branch.
- Follow code style in existing files.
- Submit pull requests with clear descriptions.
- See CONTRIBUTING.md for details.

---

## License

This project is licensed under the MIT License.  
See [`LICENSE`](LICENSE).

---

## FAQ

See [`docs/FAQ.md`](docs/FAQ.md).

---

## Roadmap

See [`docs/ROADMAP.md`](docs/ROADMAP.md).

---


## Known Issues

See [`docs/KNOWN_ISSUES.md`](docs/KNOWN_ISSUES.md).

---

## API Documentation (Swagger)

- Full OpenAPI documentation is available in [`swagger.yaml`](swagger.yaml) in the project root.
- You can view and interact with the API using [Swagger UI](https://swagger.io/tools/swagger-ui/) or import the file into Postman.
- To enable live Swagger UI in your Spring Boot app, see [`docs/SWAGGER.md`](docs/SWAGGER.md) for setup instructions.

---
## Configuration

- **application.properties:**  
  Located in `src/main/resources/`.  
  Contains database connection settings, server port, and other Spring Boot configuration options. 
- Set DB connection in `application.properties`
- Environment variables for secrets (see `.env.example`)
- API keys for external integrations (if needed)

- **.env.example:**  
  Template for environment variables such as API keys and secrets.  
  Copy this file to `.env` and fill in your actual values for local development.

- **SecurityConfig.java:**  
  Located in `src/main/java/com/sakhiya/investment/SecurityConfig.java`.  
  Configures Spring Security for the API, allowing public access to `/api/` endpoints and disabling CSRF for easier API testing.

- **PasswordEncoderConfig.java:**  
  Located in `src/main/java/com/sakhiya/investment/config/PasswordEncoderConfig.java`.  
  Registers a `BCryptPasswordEncoder` bean for secure password hashing.

---

## Validation Utility

A reusable validation class is provided in the `util` folder and is used by different service classes to ensure data integrity:

- **Purpose:**  
  Centralizes validation logic for fields such as email, date of birth, and other formats.
- **Usage:**  
  Service classes call these utility methods to validate user input before saving or updating entities.
- **Examples:**  
  - Email format validation
  - Date validation (e.g., checking valid date of birth)
  - Password strength checks

This approach helps maintain consistent validation across the project and prevents invalid data from being persisted.
--

## Technical Notes: JSON Data Storage

Some fields (like `detailsJson` in the Risk entity) store complex data as JSON strings in the database.  
Currently, manual serialization is used (with Jackson’s ObjectMapper) because most relational databases (H2, MySQL) do not natively support JSON columns.  
JPA AttributeConverters (such as `MapToJsonConverter` and `ListToJsonConverter`) are included for future improvements, allowing automatic mapping of Java collections to JSON columns if you migrate to a database with native JSON support (e.g., PostgreSQL).

This approach ensures flexibility for storing detailed calculation results and supports future enhancements.


## Useful Learning Links

See [`docs/useful-learning-links.md`](docs/useful-learning-links.md) for recommended resources.

---

## To Do List

See [`docs/TODO.md`](docs/TODO.md) for a detailed breakdown of planned project tasks.
