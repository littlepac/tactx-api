DROP table stock_prices;
DROP table pick_comments;
DROP table user_balance;
DROP table resets;
DROP table user_picks;
DROP table items;
DROP table picks;
DROP table stocks;
DROP table leaderboard_users;
DROP table leaderboards;
DROP table trading_sessions;
DROP table user_sessions;
DROP table users;

CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    created_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE user_sessions (
    user_id UUID PRIMARY KEY,
    current_token VARCHAR(255) NOT NULL,
    updated_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE trading_sessions (
    trade_date DATE PRIMARY KEY,
    active BOOLEAN NOT NULL,
    tradeable BOOLEAN NOT NULL
);

CREATE TABLE leaderboards (
    leaderboard_id UUID PRIMARY KEY,
    leaderboard_name VARCHAR(255) NOT NULL,
    creator_id UUID NOT NULL,
    created_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    private BOOLEAN NOT NULL DEFAULT FALSE,
    short_hash VARCHAR(20) NOT NULL UNIQUE,
    FOREIGN KEY (creator_id) REFERENCES users(user_id)
);

CREATE TABLE leaderboard_users (
    user_id UUID NOT NULL,
    leaderboard_id UUID NOT NULL,
    addition_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, leaderboard_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (leaderboard_id) REFERENCES leaderboards(leaderboard_id)
);

CREATE TABLE stocks
(
    ticker         VARCHAR(10)    NOT NULL,
    name           VARCHAR(225)   NOT NULL,
    PRIMARY KEY (ticker)
);

CREATE TABLE picks
(
    trade_date     DATE           NOT NULL,
    pick_id        INT            NOT NULL,
    ticker         VARCHAR(10)    NOT NULL,
    previous_open  NUMERIC(10, 4) NOT NULL,
    previous_close NUMERIC(10, 4) NOT NULL,
    pick_reason    TEXT           NOT NULL,
    final_open     NUMERIC(10, 4),
    final_close    NUMERIC(10, 4),
    PRIMARY KEY (trade_date, pick_id),
    FOREIGN KEY (trade_date) REFERENCES trading_sessions (trade_date),
    FOREIGN KEY (ticker) REFERENCES stocks (ticker)
);

CREATE TABLE user_picks (
    user_id UUID NOT NULL,
    trade_date DATE NOT NULL,
    pick_id INT NULL,
    pick_time TIMESTAMP NOT NULL,
    PRIMARY KEY (user_id, trade_date),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (trade_date,pick_id) REFERENCES picks(trade_date,pick_id)
);

CREATE TABLE resets (
    user_id UUID NOT NULL,
    trade_date DATE NOT NULL,
    PRIMARY KEY (user_id, trade_date),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (trade_date) REFERENCES trading_sessions(trade_date)
);

CREATE TABLE user_balance (
    user_id UUID NOT NULL,
    for_trade_date DATE NOT NULL,
    balance NUMERIC(15,4) NOT NULL,
    PRIMARY KEY (user_id, for_trade_date),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (for_trade_date) REFERENCES trading_sessions(trade_date)
);

CREATE TABLE items(
    item_id     INT          NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    PRIMARY KEY (item_id)
);

ALTER TABLE user_balance ADD COLUMN prev_eod_balance NUMERIC(15,4) NULL;

ALTER TABLE user_balance ADD COLUMN item_0 INT NOT NULL DEFAULT 1;
ALTER TABLE user_balance ADD COLUMN item_1 INT NOT NULL DEFAULT 1;
ALTER TABLE user_balance ADD COLUMN item_2 INT NOT NULL DEFAULT 1;
ALTER TABLE user_balance ADD COLUMN sod_holding VARCHAR(10);

ALTER TABLE user_picks ADD COLUMN item_id INT NULL;

ALTER TABLE user_picks ADD CONSTRAINT fk_item FOREIGN KEY (item_id) REFERENCES items(item_id);

CREATE TABLE pick_comments
(
    trade_date DATE NOT NULL,
    pick_id    INT NULL,
    comment    TEXT,
    FOREIGN KEY (trade_date, pick_id) REFERENCES picks (trade_date, pick_id)
);

CREATE TABLE stock_prices
(
    trade_date DATE NOT NULL,
    ticker VARCHAR(10) NOT NULL,
    open  NUMERIC(10, 4) NOT NULL,
    close NUMERIC(10, 4) NOT NULL,
    PRIMARY KEY (trade_date, ticker),
    FOREIGN KEY (ticker) REFERENCES stocks(ticker)
);

INSERT into stocks (ticker, name) values ('NVDA','NVIDIA');
INSERT into stocks (ticker, name) values ('MSFT','Microsoft');
INSERT into stocks (ticker, name) values ('AAPL','Apple');
INSERT into stocks (ticker, name) values ('GOOG','Alphabet (Google)');
INSERT into stocks (ticker, name) values ('AMZN','Amazon');
INSERT into stocks (ticker, name) values ('META','Meta Platforms (Facebook)');
INSERT into stocks (ticker, name) values ('AVGO','Broadcom');
INSERT into stocks (ticker, name) values ('BRK-B','Berkshire Hathaway');
INSERT into stocks (ticker, name) values ('TSLA','Tesla');
-- INSERT into stocks (ticker, name) values ('JPM','JPMorgan Chase');
-- INSERT into stocks (ticker, name) values ('WMT','Walmart');
-- INSERT into stocks (ticker, name) values ('ORCL','Oracle');
-- INSERT into stocks (ticker, name) values ('LLY','Eli Lilly');
-- INSERT into stocks (ticker, name) values ('V','Visa');
-- INSERT into stocks (ticker, name) values ('MA','Mastercard');
-- INSERT into stocks (ticker, name) values ('NFLX','Netflix');
-- INSERT into stocks (ticker, name) values ('XOM','Exxon Mobil');
-- INSERT into stocks (ticker, name) values ('COST','Costco');
-- INSERT into stocks (ticker, name) values ('JNJ','Johnson & Johnson');
-- INSERT into stocks (ticker, name) values ('HD','Home Depot');
-- INSERT into stocks (ticker, name) values ('PLTR','Palantir');
-- INSERT into stocks (ticker, name) values ('PG','Procter & Gamble');
-- INSERT into stocks (ticker, name) values ('ABBV','AbbVie');
-- INSERT into stocks (ticker, name) values ('BAC','Bank of America');
-- INSERT into stocks (ticker, name) values ('CVX','Chevron');
-- INSERT into stocks (ticker, name) values ('KO','Coca-Cola');
-- INSERT into stocks (ticker, name) values ('GE','General Electric');
-- INSERT into stocks (ticker, name) values ('AMD','AMD');
-- INSERT into stocks (ticker, name) values ('TMUS','T-Mobile US');
-- INSERT into stocks (ticker, name) values ('CSCO','Cisco');
-- INSERT into stocks (ticker, name) values ('PM','Philip Morris International');
-- INSERT into stocks (ticker, name) values ('WFC','Wells Fargo');
-- INSERT into stocks (ticker, name) values ('CRM','Salesforce');
-- INSERT into stocks (ticker, name) values ('IBM','IBM');
-- INSERT into stocks (ticker, name) values ('MS','Morgan Stanley');
-- INSERT into stocks (ticker, name) values ('ABT','Abbott Laboratories');
-- INSERT into stocks (ticker, name) values ('GS','Goldman Sachs');
-- INSERT into stocks (ticker, name) values ('MCD','McDonald');
-- INSERT into stocks (ticker, name) values ('INTU','Intuit');
-- INSERT into stocks (ticker, name) values ('UNH','UnitedHealth');
-- INSERT into stocks (ticker, name) values ('RTX','RTX');
-- INSERT into stocks (ticker, name) values ('BX','Blackstone Group');
-- INSERT into stocks (ticker, name) values ('DIS','Walt Disney');
-- INSERT into stocks (ticker, name) values ('AXP','American Express');
-- INSERT into stocks (ticker, name) values ('CAT','Caterpillar');
-- INSERT into stocks (ticker, name) values ('MRK','Merck');
-- INSERT into stocks (ticker, name) values ('T','AT&T');
-- INSERT into stocks (ticker, name) values ('PEP','Pepsico');
-- INSERT into stocks (ticker, name) values ('NOW','ServiceNow');
-- INSERT into stocks (ticker, name) values ('UBER','Uber');
-- INSERT into stocks (ticker, name) values ('VZ','Verizon');
-- INSERT into stocks (ticker, name) values ('GEV','GE Vernova');
-- INSERT into stocks (ticker, name) values ('TMO','Thermo Fisher Scientific');
-- INSERT into stocks (ticker, name) values ('BKNG','Booking Holdings (Booking.com)');
-- INSERT into stocks (ticker, name) values ('SCHW','Charles Schwab');
-- INSERT into stocks (ticker, name) values ('ISRG','Intuitive Surgical');
-- INSERT into stocks (ticker, name) values ('C','Citigroup');
-- INSERT into stocks (ticker, name) values ('BLK','BlackRock');
-- INSERT into stocks (ticker, name) values ('BA','Boeing');
-- INSERT into stocks (ticker, name) values ('SPGI','S&P Global');
-- INSERT into stocks (ticker, name) values ('TXN','Texas Instruments');
-- INSERT into stocks (ticker, name) values ('QCOM','QUALCOMM');
-- INSERT into stocks (ticker, name) values ('AMGN','Amgen');
-- INSERT into stocks (ticker, name) values ('BSX','Boston Scientific');
-- INSERT into stocks (ticker, name) values ('ANET','Arista Networks');
-- INSERT into stocks (ticker, name) values ('ADBE','Adobe');
-- INSERT into stocks (ticker, name) values ('NEE','Nextera Energy');
-- INSERT into stocks (ticker, name) values ('AMAT','Applied Materials');
-- INSERT into stocks (ticker, name) values ('SYK','Stryker Corporation');
-- INSERT into stocks (ticker, name) values ('PGR','Progressive');
-- INSERT into stocks (ticker, name) values ('DHR','Danaher');
-- INSERT into stocks (ticker, name) values ('GILD','Gilead Sciences');
-- INSERT into stocks (ticker, name) values ('TJX','TJX Companies');
-- INSERT into stocks (ticker, name) values ('HON','Honeywell');
-- INSERT into stocks (ticker, name) values ('DE','Deere & Company (John Deere)');
-- INSERT into stocks (ticker, name) values ('PFE','Pfizer');
-- INSERT into stocks (ticker, name) values ('COF','Capital One');
-- INSERT into stocks (ticker, name) values ('KKR','KKR & Co.');
-- INSERT into stocks (ticker, name) values ('UNP','Union Pacific Corporation');
-- INSERT into stocks (ticker, name) values ('APP','AppLovin');
-- INSERT into stocks (ticker, name) values ('APH','Amphenol');
-- INSERT into stocks (ticker, name) values ('LOW','Lowe''s Companies');
-- INSERT into stocks (ticker, name) values ('LRCX','Lam Research');
-- INSERT into stocks (ticker, name) values ('ADP','Automatic Data Processing');
-- INSERT into stocks (ticker, name) values ('CMCSA','Comcast');
-- INSERT into stocks (ticker, name) values ('VRTX','Vertex Pharmaceuticals');
-- INSERT into stocks (ticker, name) values ('MU','Micron Technology');
-- INSERT into stocks (ticker, name) values ('KLAC','KLA');
-- INSERT into stocks (ticker, name) values ('COP','ConocoPhillips');
-- INSERT into stocks (ticker, name) values ('PANW','Palo Alto Networks');
-- INSERT into stocks (ticker, name) values ('SNPS','Synopsys');
-- INSERT into stocks (ticker, name) values ('CRWD','CrowdStrike');
-- INSERT into stocks (ticker, name) values ('WELL','Welltower');
-- INSERT into stocks (ticker, name) values ('NKE','Nike');
-- INSERT into stocks (ticker, name) values ('ADI','Analog Devices');
-- INSERT into stocks (ticker, name) values ('IBKR','Interactive Brokers');
-- INSERT into stocks (ticker, name) values ('CEG','Constellation Energy');
-- INSERT into stocks (ticker, name) values ('ICE','Intercontinental Exchange');
-- INSERT into stocks (ticker, name) values ('DASH','DoorDash');
-- INSERT into stocks (ticker, name) values ('SO','Southern Company');

INSERT INTO trading_sessions (trade_date, active, tradeable) values ('2025-07-29', true, true);
INSERT INTO picks (trade_date, pick_id, ticker, previous_open, previous_close, pick_reason) values ('2025-07-29', 0, 'NVDA', 1, 2, 'The best stock');
INSERT INTO picks (trade_date, pick_id, ticker, previous_open, previous_close, pick_reason) values ('2025-07-29', 1, 'TSLA', 1, 2, 'The 2nd best stock');
INSERT INTO picks (trade_date, pick_id, ticker, previous_open, previous_close, pick_reason) values ('2025-07-29', 2, 'AAPL', 1, 2, 'The 3rd best stock');
INSERT INTO picks (trade_date, pick_id, ticker, previous_open, previous_close, pick_reason) values ('2025-07-29', 3, 'GOOG', 1, 2, 'The 4th best stock');

INSERT INTO items (item_id, name, description) values (0, '$1 Bond','By skipping investment today you receive $1');
INSERT INTO items (item_id, name, description) values (1, 'Short it!','You are shorting selling your investment pick today');
INSERT INTO items (item_id, name, description) values (2, '3X','You are investing with a 3x leverage today');