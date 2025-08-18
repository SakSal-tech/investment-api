
// Package declaration: This file is part of the com.sakhiya.investment package
package com.sakhiya.investment;

// I had issues with get clients request http://localhost:8080/api/clients postman 401 error because of Spring security configuration.
// I researched about this and I had to create this file 

// Import necessary Spring Security and configuration classes
import org.springframework.context.annotation.Bean; // Marks a method as a bean producer for Spring's context
import org.springframework.context.annotation.Configuration; // Marks this class as a configuration class
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Used to configure HTTP security
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Enables Spring Security's web security support
import org.springframework.security.web.SecurityFilterChain; // Represents the security filter chain bean


// Marks this class as a configuration class for Spring
@Configuration
// Enables Spring Security's web security features
@EnableWebSecurity
public class SecurityConfig {
    // Defines a bean for the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure HTTP security
        http
            // Set up authorization rules for HTTP requests
            .authorizeHttpRequests(auth -> auth
                // Allow all requests to endpoints starting with /api/ without authentication
                .requestMatchers("/api/**").permitAll()
                // Any other request must be authenticated
                .anyRequest().authenticated()
            )
            // Disable CSRF protection (useful for testing APIs with tools like Postman)
            .csrf(csrf -> csrf.disable());
        // Build and return the configured SecurityFilterChain
        return http.build();
    }
}
