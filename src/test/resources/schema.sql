CREATE TABLE IF NOT EXISTS daily_entry(
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    date DATE NOT NULL,
    weight DECIMAL(4, 1) NOT NULL,
    body_fat DECIMAL(3, 1) NOT NULL,
    water_percentage DECIMAL(3, 1) NOT NULL
);
CREATE INDEX daily_entry_water_percentage_idx ON daily_entry (water_percentage);

CREATE TABLE IF NOT EXISTS daily_trend(
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    date DATE NOT NULL,
    weight DECIMAL(4, 1) NOT NULL,
    body_fat DECIMAL(3, 1) NOT NULL,
    water_percentage DECIMAL(3, 1) NOT NULL
);
CREATE INDEX daily_trend_water_percentage_idx ON daily_trend (water_percentage);