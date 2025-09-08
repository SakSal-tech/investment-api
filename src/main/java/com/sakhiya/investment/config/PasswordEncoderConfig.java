// This file is part of the com.sakhiya.investment.config package, which is used for application configuration classes
package com.sakhiya.investment.config;

// Import the necessary Spring annotations and classes
import org.springframework.context.annotation.Bean; // Allows us to define a bean for the Spring context
import org.springframework.context.annotation.Configuration; // Marks this class as a configuration class for Spring
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // The password encoder implementation we want to use

// Marks this class as a configuration class so Spring will process it for bean definitions
@Configuration
public class PasswordEncoderConfig {
    // This method defines a bean for BCryptPasswordEncoder
    // Why? Spring needs to know how to create and manage a BCryptPasswordEncoder instance so it can inject it wherever needed (e.g., in UserService)
    // By annotating this method with @Bean, Spring will call it and register the returned object as a bean in the application context
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // This creates a new BCryptPasswordEncoder instance
        // BCrypt is a secure hashing algorithm for storing user passwords
        // By using this bean,  API can securely hash and verify passwords for user authentication
        return new BCryptPasswordEncoder();
    }
}
