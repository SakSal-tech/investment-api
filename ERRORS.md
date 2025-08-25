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
classCould not determine recommended JdbcType for Java type 'java.util.Map<java.lang.String, java.lang.String>'


**Solution:**

The error was in the SustainablePortfolio class, specifically with the impactTargets field (private Map<String, String> impactTargets). I fixed it by adding the required JPA mapping annotations (@ElementCollection, @CollectionTable, @MapKeyColumn, and @Column) directly above the impactTargets field in SustainablePortfolio.java. This allows Hibernate to persist the Map<String, String> field correctly.