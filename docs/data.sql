-- =====================================================
-- Sample data insertion for your investment database
-- 
-- Table descriptions:
--
-- client:
--   client_id (char(36), PK)
--   active (bit(1))
--   address (varchar(255))
--   created_at (date)
--   dob (date)
--   email (varchar(255))
--   first_name (varchar(255))
--   national_insurance_number (varchar(255))
--   post_code (varchar(255))
--   surname (varchar(255))
--   telephone (varchar(255))
--
-- portfolio:
--   portfolio_id (char(36), PK)
--   portfolio_type (varchar(31))
--   created_at (date)
--   investment_goal (varchar(255))
--   portfolio_name (varchar(255))
--   risk_level (int)
--   total_stress_test (double)
--   total_var (double)
--   total_value (decimal(38,2))
--   updated_at (date)
--   client_id (char(36), FK)
--   compliance_status (varchar(255))
--   esg_score_env (double)
--   esg_score_gov (double)
--   esg_score_social (double)
--   excluded_sectors (varchar(255))
--   impact_target_carbon (double)
--   impact_target_water (double)
--   last_updated (date)
--   overall_esg_score (double)
--   preferred_sectors (varchar(255))
--   theme_focus (varchar(255))
--
-- asset:
--   asset_id (char(36), PK)
--   name (varchar(255))
--   value (double)
--   portfolio_id (char(36), FK)
--
-- asset_price_history:
--   price_history_id (char(36), PK)
--   closing_price (double)
--   source (varchar(255))
--   trading_date (date)
--   asset_id (char(36), FK)
--
-- Notes:
-- 1. UUIDs for primary keys are generated automatically.
-- 2. asset_price_history is normally fetched automatically from AlphaVantage API.
--    Manual inserts are just examples.
-- =====================================================

-- Insert clients
INSERT INTO client (client_id, active, first_name, surname, email, address, post_code, telephone, dob, national_insurance_number, created_at)
VALUES
  (UUID(), b'1', 'Alice', 'Smith', 'alice@example.com', '123 Main St', 'AB12 3CD', '0123456789', '1985-06-15', 'AB123456C', CURRENT_DATE),
  (UUID(), b'1', 'Bob', 'Johnson', 'bob@example.com', '456 Oak Ave', 'CD34 5EF', '0987654321', '1990-09-20', 'CD987654E', CURRENT_DATE);

-- Insert portfolios
INSERT INTO portfolio (portfolio_id, portfolio_type, portfolio_name, client_id, created_at, updated_at, investment_goal, risk_level, total_value)
VALUES
  (UUID(), 'Retirement', 'Alice Portfolio', (SELECT client_id FROM client WHERE email='alice@example.com'), CURRENT_DATE, CURRENT_DATE, 'Long-term growth', 5, 100000.00),
  (UUID(), 'Wealth Management', 'Bob Portfolio', (SELECT client_id FROM client WHERE email='bob@example.com'), CURRENT_DATE, CURRENT_DATE, 'Capital preservation', 3, 50000.00);

-- Insert assets
INSERT INTO asset (asset_id, name, value, portfolio_id)
VALUES
  (UUID(), 'Apple Inc.', 150.25, (SELECT portfolio_id FROM portfolio WHERE portfolio_name='Alice Portfolio')),
  (UUID(), 'Alphabet Inc.', 2800.50, (SELECT portfolio_id FROM portfolio WHERE portfolio_name='Alice Portfolio')),
  (UUID(), 'Tesla Inc.', 720.15, (SELECT portfolio_id FROM portfolio WHERE portfolio_name='Bob Portfolio'));

-- Insert asset price history (manual insert example)
-- Note: normally fetched automatically from AlphaVantage
INSERT INTO asset_price_history (price_history_id, closing_price, source, trading_date, asset_id)
VALUES
  (UUID(), 150.25, 'Manual', '2025-09-01', (SELECT asset_id FROM asset WHERE name='Apple Inc.')),
  (UUID(), 152.30, 'Manual', '2025-09-02', (SELECT asset_id FROM asset WHERE name='Apple Inc.')),
  (UUID(), 2800.50, 'Manual', '2025-09-01', (SELECT asset_id FROM asset WHERE name='Alphabet Inc.')),
  (UUID(), 720.15, 'Manual', '2025-09-01', (SELECT asset_id FROM asset WHERE name='Tesla Inc.'));
