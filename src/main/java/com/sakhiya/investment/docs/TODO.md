# To-Do List
- [ ] Write unit tests for user managements pack
- [ ] Research and understand why use GenerationType.AUTO instead of GenerationType.UUID, change in the data model classes if it more efficient
- [ ] create HTTP requests (get,post,put and delete) ready for Postman using correct api endpoints.
- [ ] Read about Regex and validate password and email format in User class.
- [ ]  Build more tests for the validate method from user service class
- [ ] RiskService class         
         Check the setDetailsJson methods. This gave me errors in the past as JPA (Hibernate) doesn’t natively support JSON fields unless  DB column is set to a JSON type (like in PostgreSQL with jsonb). AS I am using H2, MySQL, or plain VARCHAR columns, this JSON will just be stored as a string.
        risk.setDetailsJson("{\"shockFactor\":" + shockFactor + "}");If  later need queryable JSON fields (e.g., filter by keys inside the JSON), MySQL JSON column type would be better. JPA itself doesn’t support JSON natively, but I can: Use Hibernate Types library (@Type(JsonType.class)).

- [ ] Add JUnitParams parameterised test .

- [ ]  Build client management (Client entity, repository, CRUD endpoints, field validation)
- [ ]  Build user management (User entity, repository, registration/authentication endpoints)
- [ ]  Add password and email validation logic
- [ ]  Integrate BCrypt password hashing for secure user storage
- [ ]  Configure Spring Security and JWT authentication
- [ ]  Document and resolve 401/403 errors
- [ ]  Build portfolio management (Portfolio entity, CRUD endpoints, client/user association)
- [ ]  Build sustainable portfolio features (SustainablePortfolio entity, endpoints)
- [ ]  Implement Value-at-Risk (VaR) and stress test calculations (service, endpoints, modular code)
- [ ]  Connect to external API (AlphaVantage) for asset price history import
- [ ]  Implement deduplication and date filtering for imported data
- [ ]  Refactor controllers/services to use DTO classes for clean API responses
- [ ]  Add helper methods for DRY code (date range calculation)
- [ ]  Write and update unit tests
- [ ]  Write integration tests
- [ ]  Write Mockito tests for service/repository logic
- [ ]  Validate endpoints using Postman and test cases
- [ ]  Improve commit messages for clarity and traceability
- [ ]  Add JUnitParams parameterised test
- [ ]  Create and maintain DTO classes
- [ ]  Refactor code where necessary to follow SOLID design




