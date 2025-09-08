## Known Issues

The following limitations and issues are currently known in the Risk Analysis API:

1. **Historical Data Limitations**
   - Only a small sample of historical asset prices (7 days) is bundled for testing.
   - VaR and stress test results may not be accurate for real-world scenarios without extended datasets.

2. **Simplified Risk Models**
   - Current VaR calculation uses Historical or Parametric (volatility-based) methods only.
   - No support yet for more advanced models (e.g., Monte Carlo simulation).

3. **Stress Testing Scenarios**
   - Only two predefined stress scenarios are available:
     - Market Crash (-30% asset prices)
     - Interest Rate Spike (+2% bond yields)
   - Custom user-defined stress scenarios are not yet supported.

4. **Error Handling**
   - Error responses are JSON-formatted, but additional validation messages may be required for complex cases (e.g., invalid asset sector/category).

5. **Database**
   - H2 in-memory database is used by default for testing.
   - Data is not persisted between sessions unless switched to PostgreSQL or another external DB.

6. **Performance**
   - Designed for small to medium portfolios; performance with large datasets (e.g., 10,000+ assets) has not yet been optimized.

7. **External Data**
   - Currently no integration with live financial data or ESG scoring APIs.
   - All asset data must be entered manually or via seeding.

8. **DTO Usage and Excessive Data Exposure**
   - The Asset entity currently needs a dedicated DTO to avoid returning large, nested data structures.
   - Without a DTO, Spring Boot's default serialization exposes all related tables (such as price history, portfolio, and risk records) for each asset.
   - This can result in very large JSON responses, including historical data and external API results, which may be unnecessary for most API consumers and can impact performance and security.
   - DTOs have been added for most entities to control and limit the data returned, but Asset still requires this improvement.