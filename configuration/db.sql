CREATE SCHEMA bodycomposition;
connect bodycomposition;
CREATE TABLE IF NOT EXISTS daily_entry(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL UNIQUE,
    weight DECIMAL(4, 1) NOT NULL,
    body_fat DECIMAL(3, 1) NOT NULL,
    water_percentage DECIMAL(3, 1) NOT NULL
);
CREATE TABLE IF NOT EXISTS daily_trend(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL UNIQUE,
    weight DECIMAL(4, 1) NOT NULL,
    body_fat DECIMAL(3, 1) NOT NULL,
    water_percentage DECIMAL(3, 1) NOT NULL
);