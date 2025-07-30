DROP table user_balance;
DROP table resets;
DROP table user_picks;
DROP table picks;
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
    FOREIGN KEY (trade_date) REFERENCES trading_sessions (trade_date)
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

INSERT INTO trading_sessions (trade_date, active, tradeable) values ('2025-07-29', true, true);
INSERT INTO picks (trade_date, pick_id, ticker, previous_open, previous_close, pick_reason) values ('2025-07-29', 0, 'NVDA', 1, 2, 'The best stock');
INSERT INTO picks (trade_date, pick_id, ticker, previous_open, previous_close, pick_reason) values ('2025-07-29', 1, 'TSLA', 1, 2, 'The 2nd best stock');
INSERT INTO picks (trade_date, pick_id, ticker, previous_open, previous_close, pick_reason) values ('2025-07-29', 2, 'AAPL', 1, 2, 'The 3rd best stock');
INSERT INTO picks (trade_date, pick_id, ticker, previous_open, previous_close, pick_reason) values ('2025-07-29', 3, 'GOOG', 1, 2, 'The 4th best stock');
