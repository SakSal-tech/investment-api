## To Do List

### Packages & Features

#### Client Management (`com.sakhiya.investment.portfoliomanagement.client`)
- [ ] Build client management (Client entity, repository, CRUD endpoints, field validation)
- [ ] Add validation for client fields (e.g., email, national insurance number)
- [ ] Implement client search by multiple criteria (name, postcode, registration date)
- [ ] Add bulk client import/export functionality
- [ ] Enhance client active/inactive status management

#### User Management (`com.sakhiya.investment.usermanagement`)
- [ ] Build user management (User entity, repository, registration/authentication endpoints)
- [ ] Add password and email validation logic
- [ ] Read about Regex and validate password and email format in User class
- [ ] Build more tests for the validate method from user service class
- [ ] Integrate BCrypt password hashing for secure user storage
- [ ] Configure Spring Security and JWT authentication
- [ ] Implement user roles and permissions (admin, manager, client)
- [ ] Add user activity logging and audit trail
- [ ] Improve password reset and token expiry logic
- [ ] Add user profile update endpoint
- [ ] Write unit tests for user management package

#### Portfolio Management (`com.sakhiya.investment.portfoliomanagement.portfolio`)
- [ ] Build portfolio management (Portfolio entity, CRUD endpoints, client/user association)
- [ ] Refactor portfolio DTOs for legacy and sustainable portfolios
- [ ] Add portfolio performance analytics endpoints
- [ ] Implement portfolio archiving and restoration
- [ ] Add support for multi-currency portfolios

#### Sustainable Portfolio (`com.sakhiya.investment.portfoliomanagement.sustainable`)
- [ ] Build sustainable portfolio features (SustainablePortfolio entity, endpoints)
- [ ] Integrate additional ESG data sources and APIs
- [ ] Expand compliance tracking and reporting features
- [ ] Add ESG scoring breakdown by asset/sector
- [ ] Implement automated ESG alerts and notifications

#### Asset Management (`com.sakhiya.investment.portfoliomanagement.asset`)
- [ ] Add asset type/category management
- [ ] Implement asset bulk upload and update
- [ ] Enhance asset queries (e.g., by risk, sector, region)
- [ ] Add asset depreciation and appreciation tracking

#### Asset Price History (`com.sakhiya.investment.portfoliomanagement.asset`)
- [ ] Connect to external API (AlphaVantage) for asset price history import
- [ ] Implement deduplication and date filtering for imported data
- [ ] Add endpoints for price history import from multiple sources
- [ ] Implement price history filtering by date range and source
- [ ] Add price history export (CSV/Excel)
- [ ] Improve error handling for external API failures

#### Risk Management (`com.sakhiya.investment.risk`)
- [ ] Implement Value-at-Risk (VaR) and stress test calculations (service, endpoints, modular code)
- [ ] Refactor risk calculation logic for extensibility (VaR, StressTest, etc.)
- [ ] Add support for new risk types and scenarios
- [ ] Implement risk aggregation at portfolio and client levels
- [ ] Add risk reporting and visualization endpoints
- [ ] RiskService class: Check the setDetailsJson methods. This gave me errors in the past as JPA (Hibernate) doesn’t natively support JSON fields unless DB column is set to a JSON type (like in PostgreSQL with jsonb). As I am using H2, MySQL, or plain VARCHAR columns, this JSON will just be stored as a string. If later need queryable JSON fields (e.g., filter by keys inside the JSON), MySQL JSON column type would be better. JPA itself doesn’t support JSON natively, but I can: Use Hibernate Types library (@Type(JsonType.class)). 

---

### Documentation & Learning
- [ ] Expand API documentation in `docs/` (add more examples, diagrams, and endpoint details)
- [ ] Expand `docs/useful-learning-links.md` with more tutorials and guides
- [ ] Add step-by-step setup instructions for new contributors
- [ ] Update UML diagrams and architecture docs as features evolve

---

### Testing & Quality Assurance
- [ ] Increase test coverage for all packages
- [ ] Add integration tests for external API integrations
- [ ] Implement automated API contract testing
- [ ] Set up CI/CD pipeline for automated builds and tests

---

### Miscellaneous
- [ ] Review and update all README sections for clarity and completeness
- [ ] Clean up legacy code and remove deprecated endpoints
- [ ] Regularly update CHANGELOG and ROADMAP docs


### General Project Tasks
- [ ] Research and understand why use GenerationType.AUTO instead of GenerationType.UUID, change in the data model classes if it is more efficient
- [ ] Improve commit messages for clarity and traceability
- [ ] Refactor code where necessary to follow SOLID design
- [ ] Refactor controllers/services to use DTO classes for clean API responses
- [ ] Add helper methods for DRY code (date range calculation)
- [ ] Create and maintain DTO classes
- [ ] Validate endpoints using Postman and test cases
- [ ] Write and update unit tests
- [ ] Write integration tests
- [ ] Write Mockito tests for service/repository logic
- [ ] Add JUnitParams parameterised test
- [ ] Optimize database queries for large datasets and performance
 - [ ] Write documentations
- [ ] Test again all endpoints
- [ ] Document and resolve 401/403 errors





---
