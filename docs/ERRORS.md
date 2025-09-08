# Error Log and Solutions

This file documents errors encountered during development and how they were solved.

## Example Entry

**Error:**
401 Unauthorized when accessing /api/clients in Postman

**Solution:**
Created a SecurityConfig class to allow all /api/** endpoints without authentication.Made to permit all endpoints starting with.requestMatchers("/api/**").permitAll()

**Error:**
403 Forbidden. 

**Solution:**
I had to investigate the UUID stored in mysql database and the clientId returned by Postman getAllclients were different. Before I had * The unique identifier for the client (primary key). both client_id fields are CHAR(36), to prevent mismatch. @Id @Column(name = "client_id", columnDefinition = "CHAR(36)") @org.hibernate.annotations.Type(type = "uuid-char")  <--- important! private UUID clientId = UUID.randomUUID(); // generated in Java

private UUID clientId = UUID.randomUUID(); Storing UUID as a Java UUID type with CHAR(36) in MySQL was causing Hibernate errors. Using String avoids the need for @Type so I Changed the type of clientId from UUID to String to generate a proper 36-char string that maps to CHAR(36) in MySQL.
Another problem appeared that I needed to make changes to ClientRepository and Service class. User Entity class was also changed. Basically, anywhere I used UUID for in the client management package.
---
**Error:**
403 Forbidden. I was able to list clients and delete a client but getClient is still giving 403.

**Solution:**
This was a silly mistake I spent time on this. I have forgotten to set ACCEPT on Postman to accept application/json. Also I realised I had an error on endpoint and was missing /clients http://localhost:8080/api/clients/995c2d4c-7c82-11f0-a429-84ba590b0166


**Error:**
Test was failing after I completed the user classes including password encrptions
Parameter 1 of constructor in com.sakhiya.investment.clientmanagement.UserService required a bean of type 'org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder' that could not be found.
...
No qualifying bean of type 'org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder' available: expected at least 1 bean which qualifies as autowire candidate.

**Solution:**
After researching the issue was Spring could not find a BCryptPasswordEncoder bean to inject into  UserService constructor.

I had to create passwordConfig class and define beans with @Bean methods (like password encoders, data sources, custom services, etc.)

How PasswordEncoderConfig solves it:
A configuration class in Spring is not only for defining beans like BCryptPasswordEncoder—it can be used for any application configuration that I  want Spring to manage.
By defining a @Bean method for BCryptPasswordEncoder in PasswordEncoderConfig, I tell Spring how to create and manage this object. Now, Spring can inject it wherever it’s needed ( in  UserService), and  application tests started without this error.

**Error:**

DATABASE INSERT INTO ERROR 1406 (22001): Data too long for column 'user_id' at row 1
problem I had the primary key as UUID     //@Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    
user_id is stored as a BINARY(16) (compact form of UUID, 16 bytes).

 User entity uses @GeneratedValue(strategy = GenerationType.UUID) which will produce a 36-char UUID string, but Hibernate automatically stores it as binary(16) in MySQL if I let it handle the persistence.

**Solution:**
converted UUID and stored it as String. user_id → UUID() generates a proper 36-character string that fits CHAR(36). client_id → uses the existing values from Ir client table, maintaining the one-to-one relationship.

**Error:**
Tests failing
The problem was that Spring Data JPA cannot generate queries for fields that are collections (like List<String>), maps (like Map<String, Double>), or JSON columns. Methods such as findByThemeFocusContaining, findByExcludedSectorsContaining, findByPreferredSectorsContaining, findByImpactTargetsKey, and findByEsgScoresKey attempted to query inside these fields, which is not supported by JPA/Hibernate out of the box. This caused ApplicationContext startup errors and test failures.
I commented out these methods in SustainablePortfolioRepository, and also commented out or stubbed their usages in SustainablePortfolioService and SustainablePortfolioController. This was necessary to make the application compile and pass all tests.

**Solution:**


**Error:**

Caused by: org.hibernate.MappingException: Column 'client_id' is duplicated in mapping for entity 'com.sakhiya.investment.portfoliomanagement.SustainablePortfolio' (use '@Column(insertable=false, updatable=false)' when mapping multiple properties to the same column)
        at org.hibernate.mapping.Value.checkColumnDuplication(Value.java:196)
        ...#

**Solution:**
Problem
Portfolio (the superclass) already maps a client field with @JoinColumn(name = "client_id"). SustainablePortfolio (the subclass) also declares a clientId field with @Column(name = "client_id"). Hibernate/JPA does not allow two fields in the same entity/table to map to the same column name.

Removed the clientId field and its getter/setter from SustainablePortfolio.java.
Use the inherited client field and its getter/setter from Portfolio instead.

---

## Errors and Solutions for September 2, 2025

**Error:**
Duplicate fields and methods in SustainablePortfolio (e.g., portfolioId, portfolioName, createdAt, updatedAt) caused conflicts and compilation errors.

**Solution:**
Removed redundant fields from SustainablePortfolio. Now only fields unique to SustainablePortfolio remain; all common fields are inherited from Portfolio.

**Error:**
Missing required methods (getEsgScores, getThemeFocus, setLastUpdated, etc.) in SustainablePortfolio after field cleanup caused controller/service errors.

**Solution:**
Restored all required getters/setters and mapping methods in SustainablePortfolio to match controller/service expectations.

**Error:**
Misplaced or duplicate import statements and syntax errors in SustainablePortfolio.java after manual/automated edits.

**Solution:**
Cleaned up import statements and fixed misplaced imports and syntax errors.

## My Errors and Solutions (September 1–2, 2025)

**Error:**
When I sent JSON requests to my portfolio endpoints in Postman, I kept getting 415 Unsupported Media Type or 500 Internal Server Error. This was frustrating because I thought my endpoints were correct.

**Why it happened:**
I realised that my controller methods were not always using `@RequestBody` for DTOs, and sometimes Postman was not sending the correct `Content-Type`. Also, my DTO-to-entity mapping was missing required fields, which caused deserialization or validation errors.

**How I solved it:**
I made sure every controller method that takes a request body uses `@RequestBody`, and I always set `Content-Type: application/json` in Postman. I also double-checked that all required fields are present in my DTOs and requests. This fixed the 415 and 500 errors.

**Error:**
Some API responses had null values or missing fields, especially for portfolios and sustainable portfolios.

**Why it happened:**
This was because my entity-to-DTO mapping was incomplete, and I was missing some getters/setters. Also, I had inheritance issues where the child entity didn’t have access to all the fields it needed.

**How I solved it:**
I reviewed all my mapping logic and made sure every field had a getter and setter. I fixed the inheritance so that all required fields are available in the child entities. Now, my API responses are complete and correct.

**Error:**
My tests were failing because of database schema mismatches—specifically, UUIDs were sometimes stored as CHAR(36) and sometimes as BINARY(16). Hibernate also wasn’t creating tables as I expected.

**Why it happened:**
I was using different types for UUIDs in my entities and database, which caused mapping errors. My JPA configuration was also not scanning the right packages or generating the schema as needed.

**How I solved it:**
I standardised all UUID fields to use `String` and mapped them as `CHAR(36)` in the database. I updated my JPA config to scan the correct packages and generate the schema. This made my tests pass and my database consistent.

**Error:**
After refactoring, I had missing or duplicate fields and methods in `SustainablePortfolio`, which caused compilation errors and test failures.

**Why it happened:**
I tried to clean up the class by removing duplicates, but I accidentally removed methods that the controller and service still needed. This broke the build.

**How I solved it:**
I restored all the required methods and only removed fields that were truly redundant. I made sure the controller and service logic matched the entity structure. This fixed the compilation and test errors.

**Error:**
Some Postman requests failed because the endpoint mappings were missing or incorrect.

**Why it happened:**
I had typos or missing annotations in my controller methods, and sometimes the Postman collection didn’t match my actual endpoints.

**How I solved it:**
I carefully reviewed all my controller endpoint paths and HTTP method annotations. I also updated my Postman collection to match the actual endpoints. Now, all my requests work as expected.
Spring Boot tests failed to start, and the application context could not load. The error log showed:

    Could not create query for public abstract java.util.List com.sakhiya.investment.portfoliomanagement.SustainablePortfolioRepository.findByImpactTargetKey(java.lang.String); Reason: Validation failed for query for method ...

and

    org.hibernate.type.descriptor.java.spi.JdbcTypeRecommendationException: Could not determine recommended JdbcType for Java type 'java.util.Map<java.lang.String, java.lang.String>'

**What was happening:**
I was using Map fields (e.g., `private Map<String, String> impactTargets;`) in my JPA entity (`SustainablePortfolio`). I had marked  this on purpose unannotated as it is a sub class I originally wanted this to be a field as portfolio type, which meant JPA did not persist them. However, my repository was trying to run JPQL queries on these fields. JPA cannot query fields that are not persisted, and it also cannot handle Map fields without the correct annotation.

**Solution:**
I annotated my Map fields with `@ElementCollection` (e.g., `@ElementCollection private Map<String, String> impactTargets;`). This tells JPA to persist the map in a separate table and enables JPQL queries on the map's keys and values. I also removed any `@Transient` annotation from these fields so this becomes persistem. After this change, my repository JPQL queries (using `KEY(mapField) = :key`) worked, and the tests passed.

**Technical changes:**
- Added `@ElementCollection` to all Map fields that need to be persisted and queried.
- Removed `@Transient` from those fields.
- Ensured my repository methods use JPQL with `@Query` and `KEY(mapField)` for querying map keys.
- Updated my Postman requests to send map fields as JSON objects.

This fixed the test failures and allowed the application to start and run queries on map fields as intended.

## Errors and Solutions for September 2, 2025 (Afternoon)

**Error:**
MySQL foreign key constraint error: Referencing column 'asset_id' and referenced column 'asset_id' in foreign key constraint are incompatible.

**Solution:**
Discovered that the `risk` table had an extra `id` column (binary(16)) as primary key, while the entity only used `risk_id` (char(36)). Dropped the `id` column and set `risk_id` as the primary key. Ensured both `asset.asset_id` and `risk.asset_id` are `char(36)` and match exactly. Dropped and recreated the foreign key constraint.

**Error:**
Hibernate/Jackson serialization error: Cannot handle managed/back reference 'defaultReference': no back reference property found from type `java.util.List<com.sakhiya.investment.riskmanagement.Risk>`

**Solution:**
Fixed the use of `@JsonManagedReference` and `@JsonBackReference` in `Asset` and `Risk` entities. Only `@JsonManagedReference` is on the parent (`Asset.risks`), and only `@JsonBackReference` is on the child (`Risk.asset`).

**Error:**
MySQL syntax error: near 'CLOB' at line 1 when creating or altering the `risk` table.

**Solution:**
Removed `columnDefinition = "CLOB"` from the `detailsJson` field in `Risk.java` and used only `@Lob`, letting Hibernate map to the correct type (`LONGTEXT`) for MySQL.

**Error:**
Data truncation: Data too long for column 'asset_id' at row 1 when inserting into `asset` table.

**Solution:**
Ensured that all UUIDs are stored as `String` and mapped as `CHAR(36)` in both entity and database schema for all relevant tables and foreign keys.

**Error:**
415/403 errors on API requests (Forbidden/Unsupported Media Type).

**Solution:**
Set correct `Content-Type: application/json` in Postman, ensured all controller methods use `@RequestBody`, and checked Spring Security configuration to allow unauthenticated access for development.

**Error:**
PUT/POST JSON field mismatch (e.g., `riskType` instead of `type`, `valueAtRisk` instead of `value`)

**Solution:**
Corrected all JSON request bodies to use the exact field names as defined in the entity classes.

---
## Errors and Solutions for 4/09/2025

**Error:**
API endpoint `/api/asset-price-history/asset/{assetId}` returned extremely large and deeply nested JSON output, including asset, portfolio, and client details for every price history record.

**Solution:**
Created and used a dedicated DTO (`AssetPriceHistoryDTO`) for price history responses, including only the necessary fields (trading date, closing price, source, assetId, assetName). Updated the controller to map entities to DTOs, preventing recursive serialization and large output.

**Error:**
API returned months of price history data, making it hard to focus on recent prices.

**Solution:**
Updated the controller to accept optional `startDate` and `endDate` query parameters, defaulting to the last 7 days if not provided. Also updated the import method to only fetch and save the last 7 days of price data from AlphaVantage, keeping the database focused on recent prices.

**Error:**
Duplicate import methods in `AssetPriceHistoryController` caused confusion about which method was correct for importing price history.

**Solution:**
Removed the duplicate method and kept only the version that filters and saves the last 7 days of price history, ensuring only recent data is imported and stored.

**Error:**
Duplicate price history records for the same asset and trading date were being saved, causing repeated entries in the database and API output.

**Solution:**
Updated the service logic to check for existing records before saving new price history entries, preventing duplicates and ensuring only one record per asset per date is stored.
**Error:**

403 Forbidden error when calling POST /api/asset-price-history/import/{assetId}/{symbol} to import asset price history.
Solution:
I added the missing @PostMapping annotation to the import endpoint in AssetPriceHistoryController.java, allowing POST requests and resolving the 403 error.
**Error:**

GET endpoints (/api/asset-price-history, /api/asset-price-history/{id}, /api/asset-price-history/asset/{assetId}, /api/asset-price-history/symbol/{symbol}) returned nested entity data, including asset and portfolio objects, resulting in large and confusing API responses.
Solution:
I refactored all GET endpoints to return DTOs (AssetPriceHistoryDTO) using a helper method, so only clean, relevant fields are exposed in the API response and nested data is prevented.

**Error:**

Importing price history from AlphaVantage via POST /api/asset-price-history/import/{assetId}/{symbol} fetched and stored excessive historical data, making the database unnecessarily large.
Solution:
I implemented a helper method to calculate a 7-day date range and filtered the imported data to only include the last 7 days, keeping the database focused on recent prices.

**Error:**
Duplicate records appeared in asset price history after multiple imports using POST /api/asset-price-history/import/{assetId}/{symbol}.
Solution:
I added deduplication logic in the service layer to check for existing records before saving new price history entries, ensuring only unique records are stored.

**Error:**
GET endpoints (/api/asset-price-history, /api/asset-price-history/asset/{assetId}) returned empty lists even when data existed in the database.
Solution:
I created custom repository methods for date filtering and used a helper method in the controller to handle optional date parameters, ensuring the correct data is returned.

**Error:**
Some controller methods for GET endpoints (/api/asset-price-history, /api/asset-price-history/{id}, /api/asset-price-history/asset/{assetId}, /api/asset-price-history/symbol/{symbol}) were returning entities instead of DTOs, leading to inconsistent API responses.
Solution:
I reviewed and refactored all GET methods in AssetPriceHistoryController.java to consistently return DTOs using the helper method.

**Error:**
RiskServiceTest fails: 
java.lang.NullPointerException: Cannot invoke "com.sakhiya.investment.portfoliomanagement.asset.AssetHistoryService.getHistoricalReturns(String)" because "this.assetHistoryService" is null
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended. [WARN] Not enough price history for assetId: test. Returning empty returns list.

Solution:
1. Added the constructor to RiskService to take AssetHistoryService as a parameter
2. I had @MOck and @InjectMocks in reverse places infront of the wrong class that I want to mock
@Mock is used to create a mock (fake) version of a dependency. I changed to @InjectMocks on the class I wanted to test; Mockito will automatically inject any mocks into it. In my test:@Mock private AssetPriceHistoryRepository assetPriceHistoryRepository; This created a mock repository. @InjectMocks private AssetHistoryService assetHistoryService; This created a real AssetHistoryService and injects the mock repository into it.
@Mock private RiskService riskService; This created a mock RiskService (not a real one).
I should have used @InjectMocks for the class too test (RiskService), and @Mock for its dependencies (AssetHistoryService, etc).This way, Mockito creates a real RiskService and injects the mocks into it.
In the future, only use @InjectMocks on the class under test, and @Mock for its dependencies.

**Error:**
Risk calculation was failing in tests with NullPointerException when calling:
assetHistoryService.getHistoricalReturns(String assetId)
Symptoms:

RiskServiceTest failed.

Asset historical returns were null.

OpenJDK warning: "Not enough price history for assetId: test..."

Cause:

AssetHistoryService dependency in RiskService was not injected correctly.

Mockito mocks were applied incorrectly: @Mock and @InjectMocks were swapped.

RiskService constructor expected a real AssetHistoryService, but none was provided.

Solution:

Correct Mockito annotations:

@Mock
private AssetHistoryService assetHistoryService; // dependency

@InjectMocks
private RiskService riskService; // class under test


@InjectMocks is always used on the class I are testing (RiskService).

@Mock is used for all its dependencies (AssetHistoryService, repositories, etc.).

Provide test data via mock:

when(assetHistoryService.getHistoricalReturns("test"))
    .thenReturn(List.of(0.02, 0.03, -0.01));


Constructor injection:

RiskService should have a constructor accepting AssetHistoryService.

Mockito automatically injects the mock into the constructor when using @InjectMocks.

Run test:

RiskService now calculates risk using mocked historical returns.

NullPointerException no longer occurs.

Warnings about insufficient price history are expected in test logs but do not fail the test.

Result:

RiskServiceTest runs successfully.

Risk calculation logic works with injected mock data.

Future tests can mock different return values for various asset scenarios.

Error
Parameterized JUnit test for dummy client generation was failing. Symptoms included:

Test not running or reporting no parameters supplied.

NullPointerException when trying to use injected dependencies inside the test method.

Mismatched types in CSV source vs method parameters.

Cause:

@ParameterizedTest annotation used incorrectly or missing.

@CsvSource or other parameter provider had values not matching method parameter types.

Mocks and @InjectMocks/@Mock were applied to the wrong objects.

Solution:

Correct JUnit annotations:

@ParameterizedTest
@CsvSource({
    "Client1, client1@example.com",
    "Client2, client2@example.com"
})
void testDummyClientGeneration(String name, String email) {
    // Test logic here
}


Ensure method parameters match CSV source types:

String in CSV maps to String in method parameters.

Any primitive type (int, boolean, etc.) in CSV must match the parameter type exactly.

Properly inject mocks and tested class:

@Mock
private ClientRepository clientRepository;

@InjectMocks
private ClientService clientService; // class under test


@InjectMocks goes on the class I are testing.

@Mock goes on its dependencies.

Verify constructor injection if used:

If ClientService has a constructor with ClientRepository or other dependencies, Mockito automatically injects mocks via @InjectMocks.

Run test with JUnit 5 (Jupiter) engine to support parameterized tests.

Result:

Test ran for each row in the CSV source.

Dependencies were correctly mocked.

Dummy client generation test passed without NullPointerException.

# Error Log and Solutions

This file documents errors encountered during development and how they were solved.

---

## September 6, 2025

**Error:**  
Client was not updating in the database.  
**Cause:**  
The `createdAt` field in the Client entity was missing a setter, so updates failed when Hibernate tried to set this value (even though it is auto-inserted).  
**Solution:**  
Added a setter for `createdAt` in the Client entity. Now updates work as expected.

---

**Error:**  
User login and password reset were failing.  
**Cause:**  
- Login was failing due to incorrect request body field names (`rawPassword` was required, not `password`).
- Password reset was failing due to mismatched or missing reset tokens, and password validation was rejecting passwords longer than 15 characters.
- Error handling was unclear, returning 404 or 403 without specific messages.
**Solution:**  
- Updated login endpoint to use `UserDTO` and require `rawPassword` in the request body.
- Improved password validation logic and clarified requirements (8–15 characters, at least one uppercase, one lowercase, one digit, one special character).
- Added logging and improved error messages for reset token and username mismatches.
- Fixed the reset password flow to clear the reset token after successful password change.
- Verified that the reset token and username match in the database before allowing password reset.

---

**Error:**  
API was returning 404 for valid reset password requests.  
**Cause:**  
Password validation was failing due to length or missing required character types, or the reset token was not found for the user.  
**Solution:**  
- Added debug logging to `findByResetToken` and `resetPassword` methods in `UserService`.
- Improved error handling in `UserController` to return specific error messages for token not found, invalid username, or password validation failure.
- Ensured the reset token and username are checked together before updating the password.

---

**Summary:**  
- Fixed client update issues by adding missing setters.
- Standardized user login and password reset flows using DTOs and clear validation.
- Improved error handling and logging for easier debugging.
- Password reset now works reliably with proper validation and token clearing.

**getAppPortfolio returns so much unwanted data**  
Since connecting to external API, the getAllPortfolio request, I am getting back all related data from asset, clients and risk data

**Cause:**  

Why I return the full SustainablePortfolio entity from  controller, Jackson serializes all fields and relationships, including the assets list and any nested objects.
Relationships and Fields
@ManyToOne private Client client;
This means each portfolio is linked to a client entity.
@OneToMany private List<Asset> assets;
This means each portfolio contains a list of asset entities.
Why I Get Nested Data
When I return a Portfolio entity from my controller, Jackson will serialize:
The client object (with all its fields)
The assets list (with all asset fields)
Any other fields in Portfolio
This is why I see all related data (client, assets, etc.) in my API response.
This is standard JPA/Jackson behavior and not related to the external API usage for stock prices.
**Solutions:**  
