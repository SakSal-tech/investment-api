# Table Descriptions

This document explains the main database tables used in the Risk Analysis API.

## client
- **Purpose:** Stores client details.
- **Fields:** client_id (PK), name, contact, createdAt, updatedAt
Stores client details.

| Field                     | Type         | Null | Key | Default | Extra |
|---------------------------|-------------|------|-----|---------|-------|
| client_id                 | char(36)    | NO   | PRI | NULL    |       |
| active                    | bit(1)      | NO   |     | NULL    |       |
| address                   | varchar(255)| YES  |     | NULL    |       |
| created_at                | date        | YES  |     | NULL    |       |
| dob                       | date        | YES  |     | NULL    |       |
| email                     | varchar(255)| YES  |     | NULL    |       |
| first_name                | varchar(255)| YES  |     | NULL    |       |
| national_insurance_number | varchar(255)| YES  |     | NULL    |       |
| post_code                 | varchar(255)| YES  |     | NULL    |       |
| surname                   | varchar(255)| YES  |     | NULL    |       |
| telephone                 | varchar(255)| YES  |     | NULL    |       |

---


## portfolio
- **Purpose:** Stores portfolio information.
- **Fields:** portfolio_id (PK), portfolioName, client_id (FK), createdAt, updatedAt, ESG-related fields (for sustainable portfolios)


| Field                  | Type          | Null | Key | Default | Extra |
|------------------------|---------------|------|-----|---------|-------|
| portfolio_type         | varchar(31)   | NO   |     | NULL    |       |
| portfolio_id           | char(36)      | NO   | PRI | NULL    |       |
| created_at             | date          | YES  |     | NULL    |       |
| investment_goal        | varchar(255)  | YES  |     | NULL    |       |
| portfolio_name         | varchar(255)  | YES  |     | NULL    |       |
| risk_level             | int           | YES  |     | NULL    |       |
| total_stress_test      | double        | YES  |     | NULL    |       |
| total_var              | double        | YES  |     | NULL    |       |
| total_value            | decimal(38,2) | YES  |     | NULL    |       |
| updated_at             | date          | YES  |     | NULL    |       |
| client_id              | char(36)      | YES  | MUL | NULL    |       |
| compliance_status      | varchar(255)  | YES  |     | NULL    |       |
| esg_score_env          | double        | YES  |     | NULL    |       |
| esg_score_gov          | double        | YES  |     | NULL    |       |
| esg_score_social       | double        | YES  |     | NULL    |       |
| excluded_sectors       | varchar(255)  | YES  |     | NULL    |       |
| impact_target_carbon   | double        | YES  |     | NULL    |       |
| impact_target_water    | double        | YES  |     | NULL    |       |
| last_updated           | date          | YES  |     | NULL    |       |
| overall_esg_score      | double        | YES  |     | NULL    |       |
| preferred_sectors      | varchar(255)  | YES  |     | NULL    |       |
| theme_focus            | varchar(255)  | YES  |     | NULL    |       |

---

## sustainable_portfolio
- **Purpose:** Inherits from portfolio. Contains additional fields for ESG scoring and compliance.Already exists as the parent table.
Stores all common fields: portfolio_id, portfolio_name, client_id, risk_level, etc.No duplication of child fields here.
The fields annotated such as excludedSectors and preferredSectors) are stored in separate join tables rather than as delimited strings in a single column. This is a normalised relational database design, where each value in the list gets its own row in the join table, improving data integrity, queryability, and scalability.

- **Fields:** Only unique fields for ESG and compliance; common fields inherited from portfolio
Created automatically for the subclass SustainablePortfolio.
Stores only fields specific to sustainable portfolios, for example:
   - esg_score_env, esg_score_gov, esg_score_social
   - theme_focus, preferred_sectors, excluded_sectors
   - impact_target_carbon, impact_target_water

    Has a foreign key relationship to portfolio.portfolio_id.
    Primary key: same as portfolio_id (shared with parent table).

## asset
- **Purpose:** Stores asset information within portfolios.
- **Fields:** asset_id (PK), assetName, assetType (Stock/Bond/Fund), portfolio_id (FK), value, riskLevel


| Field        | Type         | Null | Key | Default | Extra |
|--------------|--------------|------|-----|---------|-------|
| asset_id     | char(36)     | NO   | PRI | NULL    |       |
| name         | varchar(255) | YES  |     | NULL    |       |
| value        | double       | YES  |     | NULL    |       |
| portfolio_id | char(36)     | YES  | MUL | NULL    |       |

---

## risk
- **Purpose:** Stores calculated risk metrics for assets or portfolios.
- **Fields:** risk_id (PK), asset_id (FK), varAmount, varPercentage, stressScenario, stressLoss
ysql> describe risk;
+------------------+--------------+------+-----+---------+-------+
| Field            | Type         | Null | Key | Default | Extra |
+------------------+--------------+------+-----+---------+-------+
| calculation_date | date         | YES  |     | NULL    |       |
| confidence_level | double       | YES  |     | NULL    |       |
| currency         | varchar(255) | YES  |     | NULL    |       |
| description      | varchar(255) | YES  |     | NULL    |       |
| details_json     | longtext     | YES  |     | NULL    |       |
| scenario         | varchar(255) | YES  |     | NULL    |       |
| time_horizon     | varchar(255) | YES  |     | NULL    |       |
| type             | varchar(255) | YES  |     | NULL    |       |
| value            | double       | YES  |     | NULL    |       |
| asset_id         | char(36)     | YES  | MUL | NULL    |       |
| risk_id          | char(36)     | NO   | PRI | NULL    |       |
+------------------+--------------+------+-----+---------+-------+


---

## asset_price_history
- **Purpose:** Stores historical asset prices for risk calculations.
- **Fields:** id (PK), asset_id (FK), tradingDate, closingPrice, source

+------------------+--------------+------+-----+---------+-------+
| Field            | Type         | Null | Key | Default | Extra |
+------------------+--------------+------+-----+---------+-------+
| price_history_id | char(36)     | NO   | PRI | NULL    |       |
| closing_price    | double       | YES  |     | NULL    |       |
| source           | varchar(255) | YES  |     | NULL    |       |
| trading_date     | date         | YES  |     | NULL    |       |
| asset_id         | char(36)     | NO   | MUL | NULL    |       |
+------------------+--------------+------+-----+---------+-------+

