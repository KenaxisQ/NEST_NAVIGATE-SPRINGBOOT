CREATE TABLE IF NOT EXISTS amenity (
                         id CHAR(36) PRIMARY KEY DEFAULT (UUID()), -- Store UUID as a 36-character string
                         name VARCHAR(255) NOT NULL,
                         category VARCHAR(50) NOT NULL,
                         sub_category VARCHAR(50) NOT NULL
);
