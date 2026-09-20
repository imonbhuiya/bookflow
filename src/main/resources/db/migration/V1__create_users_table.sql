CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,

                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       phone VARCHAR(255) NOT NULL,

                       date_of_birth DATE NOT NULL,
                       gender VARCHAR(20) NOT NULL,

                       street VARCHAR(255) NOT NULL,
                       city VARCHAR(255) NOT NULL,
                       postal_code VARCHAR(255) NOT NULL,
                       country VARCHAR(255) NOT NULL,

                       role VARCHAR(20) NOT NULL DEFAULT 'USER',
                       enabled BOOLEAN NOT NULL DEFAULT TRUE,

                       created_at DATETIME NOT NULL,
                       updated_at DATETIME NOT NULL
);