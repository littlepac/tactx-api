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
    active BOOLEAN NOT NULL DEFAULT TRUE
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

CREATE TABLE picks (
    trade_date DATE NOT NULL,
    pick_id INT NOT NULL,
    ticker VARCHAR(10) NOT NULL,
    previous_change NUMERIC(10,4),
    pick_reason TEXT,
    final_open NUMERIC(10,4),
    final_close NUMERIC(10,4),
    PRIMARY KEY (trade_date, pick_id),
    FOREIGN KEY (trade_date) REFERENCES trading_sessions(trade_date)
);

CREATE TABLE user_picks (
    user_id UUID NOT NULL,
    trade_date DATE NOT NULL,
    pick_id INT NOT NULL,
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
    for_trading_date DATE NOT NULL,
    balance NUMERIC(15,4) NOT NULL,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (for_trading_date) REFERENCES trading_sessions(trade_date)
);
