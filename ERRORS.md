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
After researching the issue was Spring could not find a BCryptPasswordEncoder bean to inject into your UserService constructor.

I had to create passwordConfig class and define beans with @Bean methods (like password encoders, data sources, custom services, etc.)

How PasswordEncoderConfig solves it:
A configuration class in Spring is not only for defining beans like BCryptPasswordEncoder—it can be used for any application configuration that you want Spring to manage.
By defining a @Bean method for BCryptPasswordEncoder in PasswordEncoderConfig, you tell Spring how to create and manage this object. Now, Spring can inject it wherever it’s needed ( in  UserService), and  application tests started without this error.

**Error:**

DATABASE INSERT INTO ERROR 1406 (22001): Data too long for column 'user_id' at row 1
problem I had the primary key as UUID     //@Id
    //@GeneratedValue(strategy = GenerationType.UUID)
    
user_id is stored as a BINARY(16) (compact form of UUID, 16 bytes).

 User entity uses @GeneratedValue(strategy = GenerationType.UUID) which will produce a 36-char UUID string, but Hibernate automatically stores it as binary(16) in MySQL if you let it handle the persistence.

**Solution:**
converted UUID and stored it as String. user_id → UUID() generates a proper 36-character string that fits CHAR(36). client_id → uses the existing values from your client table, maintaining the one-to-one relationship.

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

**Error:**
Confusion about the difference between updatedAt and lastUpdated fields.

**Solution:**
Clarified that updatedAt (inherited from Portfolio) is the standard JPA auditing field, while lastUpdated (if present) is a custom field. Recommended using only updatedAt for consistency unless a custom string is needed.

All tests are now passing and the codebase is clean.

---

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


change to accept client id instead of client object