package com.sakhiya.investment.riskmanagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.sakhiya.investment.portfoliomanagement.asset.AssetHistoryService;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class RiskServiceParameterisedTest {

    private static final double VAR_TOLERANCE = 0.01; // 1% tolerance for VaR calculation.sets the allowable margin of
                                                      // error for comparing expected and actual VaR values in test
                                                      // assertions

    @Mock
    private AssetHistoryService assetHistoryService;

    @InjectMocks
    private RiskService riskService;

    /**
     * Parameterized test for varCalculator() using fake returns list dynamically
     * generated from mean and standard deviation.
     * Each CSV row represents a different test scenario:
     * - assetId: identifier for the asset (mocked)
     * - assetValue: current value of the asset
     * - confidenceLevel: VaR confidence level
     * - timeHorizonDays: VaR time horizon in days
     * - expectedMean: mean of synthetic returns
     * - expectedStdDev: standard deviation of synthetic returns
     * - expectedVar: expected VaR value for the parameters
     */
    @ParameterizedTest
    @CsvSource({
            // Test case: simple symmetric returns around 0, 95% confidence, 1 day
            "asset1, 10000, 0.95, 1, 0.0, 0.05, -824.9999999999999",

            // Test case: 99% confidence, 1 day, same symmetric returns
            "asset2, 10000, 0.99, 1, 0.0, 0.05, -1164.9999999999998",

            // Test case: positive mean return, moderate volatility, 1 day
            "asset3, 50000, 0.95, 1, 0.01, 0.02, -1129.9999999999998",

            // Test case: negative mean return, low volatility, 1 day
            "asset4, 20000, 0.95, 1, -0.01, 0.01, -629.9999999999999",

            // Test case: multi-day horizon (5 days) to check sqrt scaling of VaR
            "asset5, 10000, 0.95, 5, 0.0, 0.05, -1842.9190298636276"
    })

    void testVarCalculatorParameterized(
            String assetId,
            double assetValue,
            double confidenceLevel,
            int timeHorizonDays,
            double expectedMean,
            double expectedStdDev,
            double expectedVar) {

        // Generating fake returns are centered around expectedMean and spaced ±offset
        // from it.
        /*
         * These returns need to reflect the expected mean (expectedMean) and expected
         * volatility (expectedStdDev).
         * offset tells us how far the synthetic returns should be spread from the mean
         * so that they have the right variability.
         * picking three points: One below the mean (mean - offset), one at the mean
         * (mean) and one above the mean (mean + offset)
         */
        double offset; // value based on a given expectedStdDev (expected standard deviation).
        if (expectedStdDev == 0) {
            offset = 0;// if expectedStdDev is zero. If it is, the offset is set to zero, which makes
                       // sense because if there is no expected variation, no offset is needed
        } else {
            // For three synthetic points, offset = stddev * sqrt(3/2) ensures the sample
            // variance matches expectedStdDev.
            // https://stats.stackexchange.com/questions/25848/variance-of-numbers-x-a-x-b-x-c
            offset = expectedStdDev * Math.sqrt(3 / 2.0);
        }
        // Three points are chosen (mean - offset, mean, mean + offset) so that:
        // The sample mean matches expectedMean and the sample standard deviation
        // matches expectedStdDev.
        // This ensures the synthetic data accurately reflects the test scenario for VaR
        // calculation.
        List<Double> syntheticReturns = List.of(expectedMean - offset, expectedMean, expectedMean + offset);

        // Mocking historical returns. varCalculator calls
        // assetHistoryService.getHistoricalReturns(assetId), don't run the real method.
        // just give it this fake syntheticReturnslist I prepared.
        when(assetHistoryService.getHistoricalReturns(assetId)).thenReturn(syntheticReturns);

        // Calling the service method, the real method in RiskService
        double actualVar = riskService.varCalculator(assetId, assetValue, confidenceLevel, timeHorizonDays);

        // Dynamically calculate expected VaR
        double mean;
        if (syntheticReturns.isEmpty()) {
            mean = 0.0;
        } else {
            double sum = 0.0;
            for (double r : syntheticReturns)
                sum += r;
            mean = sum / syntheticReturns.size();
        }
        //Calculate the standard deviation (spread around the mean). Thepurpose is to measure how volatile the returns are — how much they vary around the mean
        double stddev;
        int n = syntheticReturns.size();
        if (n <= 1) {
            stddev = 0.0; // can't compute sample stddev with < 2 points
        } else {
            double sumSq = 0.0;
            for (double r : syntheticReturns) {
                double d = r - mean;//Subtract the mean from each return.
                sumSq += d * d;//Square the difference so negatives don't cancel out
            }
            double variance = sumSq / (n - 1); // Divide by (n - 1) to getsample variance
            stddev = Math.sqrt(variance);//Take square root to get standard deviation
        }

        double z = getZScore(confidenceLevel);

        //// Recalculate VaR in the test (mean, stddev, z, horizon) to comapre it to
        //// actual
        double expectedVaR = assetValue * (mean + z * stddev * Math.sqrt(timeHorizonDays));

        // Assertion allowing 1% tolerance for floating-point calculations
        assertEquals(
                expectedVaR,
                actualVar,
                VAR_TOLERANCE * Math.abs(expectedVaR),
                "VaR calculation mismatch for asset: " + assetId);
    }

    // Helper to get Z-score for confidence level
    private double getZScore(double confidenceLevel) {
        if (confidenceLevel == 0.99)
            return -2.33;
        if (confidenceLevel == 0.95)
            return -1.65;
        if (confidenceLevel == 0.9)
            return -1.2816;
        throw new IllegalArgumentException("Unsupported confidence level: " + confidenceLevel);
    }
}
