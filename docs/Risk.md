## Risk Calculations

The Risk Analysis API provides two main methods for assessing portfolio risk:

### 1. Value at Risk (VaR)
- **Purpose:** Estimates the maximum potential loss of a portfolio over a given time period at a specified confidence level (e.g., 95%).
- **Method:** Historical Method using past price data.
- **Inputs:** Portfolio ID, confidence level, historical period (e.g., last 30 days).
- **Outputs:** 
  - VaR amount in currency
  - VaR percentage
- **Benefit:** Helps investors understand the potential downside of their investments under normal market conditions.

### 2. Stress Testing
- **Purpose:** Simulates extreme market scenarios to see how a portfolio would perform under adverse conditions.
- **Predefined Scenarios:**
  - **Market Crash:** e.g., 30% drop in asset prices
  - **Interest Rate Spike:** e.g., 2% increase in bond yields
- **Outputs:** 
  - Scenario name
  - Portfolio value before and after the event
  - Percentage loss
- **Benefit:** Provides insight into portfolio vulnerability to extreme market events.

> All risk calculations use historical asset price data stored in the database. For testing, recent 7-day historical data is included, but developers can expand the dataset or connect external APIs for more comprehensive analysis.
