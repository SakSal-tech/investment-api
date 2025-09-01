
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
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Allow all requests for debugging and test compatibility
            )
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
