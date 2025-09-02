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
