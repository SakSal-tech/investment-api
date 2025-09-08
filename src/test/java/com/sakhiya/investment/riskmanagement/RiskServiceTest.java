package com.sakhiya.investment.riskmanagement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sakhiya.investment.portfoliomanagement.asset.AssetHistoryService;
import com.sakhiya.investment.portfoliomanagement.asset.AssetPriceHistoryRepository;

public class RiskServiceTest {
     // Tells Mockito to create a mock UserRepository. Mockito will create a fake
    // version of UserRepository for the test.
    // To test service without needing a real database
    @Mock  
    private AssetPriceHistoryRepository assetPriceHistoryRepository;
    @Mock
    private AssetHistoryService assetHistoryService ;
    @InjectMocks
    private RiskService riskService;
    
    @BeforeEach // Runs before each test method
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes the mocks and injects them
    }

    @Test
    void testCalculateVaR() {
        String assetID = "test";
   
        double assetValue = 200.0;
        double confidenceLevel = 0.9;
        int timeHorizonDays = 7;
        
        List<Double> returns = new ArrayList<>();
        // MOCKING RESULTS FROM getHistoricalReturns
        returns.add(340.0);
        returns.add(205.0);
        returns.add(100.5);
        double expectedMean = 215.16666667;   
        double expectedStandardDev = 120.07324;
        double expectedconfidenceMultiplier   = -1.2816;
        when(assetHistoryService.getHistoricalReturns(assetID)).thenReturn(returns);
       // when(assetHistoryService.getHistoricalReturns(anyString())).thenReturn(returns);

        double results = riskService.varCalculator(assetID, assetValue, confidenceLevel, timeHorizonDays);
        double expectedValue = assetValue * (expectedMean + expectedconfidenceMultiplier * expectedStandardDev * Math.sqrt(timeHorizonDays) );
        // added delta  so small tolerance value that allows  test to pass even if there are tiny differences between the expected and actual values due to floating-point rounding error
        assertEquals(results, expectedValue,002 );

    }

    @Test
    void testVarCalculator() {

    }
}

/*List<Double> returns = new ArrayList<>();
returns.add(340.0);
returns.add(205.0);
returns.add(100.5);

// Calculate expected values using the same logic as RiskService
double expectedMean = calculateMean(returns);
double expectedStandardDev = calculateStandardDev(returns);
double expectedconfidenceMultiplier = -1.2816; // or use getZScore(confidenceLevel) if available

double expectedValue = assetValue * (expectedMean + expectedconfidenceMultiplier * expectedStandardDev * Math.sqrt(timeHorizonDays));
double actualValue = riskService.varCalculator(assetID, assetValue, confidenceLevel, timeHorizonDays);

assertEquals(expectedValue, actualValue, 0.0001); // Use a delta for floating-point comparison

// Helper methods (copy from RiskService)
private double calculateMean(List<Double> returns) {
    if (returns == null || returns.isEmpty()) return 0;
    double sum = 0;
    for (double r : returns) sum += r;
    return sum / returns.size();
}

private double calculateStandardDev(List<Double> returns) {
    if (returns == null || returns.isEmpty()) return 0;
    if (returns.size() <= 1) return 0;
    double mean = calculateMean(returns);
    double sumSquares = 0;
    for (double r : returns) sumSquares += Math.pow(r - mean, 2);
    return Math.sqrt(sumSquares / (returns.size() - 1));
} */