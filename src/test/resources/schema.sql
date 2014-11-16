CREATE TABLE IF NOT EXISTS daily_entry(
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    date DATE NOT NULL,
    weight DECIMAL(4, 1) NOT NULL,
    body_fat DECIMAL(3, 1) NOT NULL,
    water_percentage DECIMAL(3, 1) NOT NULL
);

CREATE TABLE IF NOT EXISTS daily_trend(
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    date DATE NOT NULL,
    weight DECIMAL(4, 1) NOT NULL,
    body_fat DECIMAL(3, 1) NOT NULL,
    water_percentage DECIMAL(3, 1) NOT NULL
);