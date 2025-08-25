package com.sakhiya.investment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
// for mock tests on dummy data 
// Use the "test" profile so Spring Boot loads the H2 in-memory database 
// configuration from src/test/resources/application.properties instead of the main MySQL database
@ActiveProfiles("test")
class InvestmentApplicationTests {

    @Test
    void contextLoads() {
    }

}
