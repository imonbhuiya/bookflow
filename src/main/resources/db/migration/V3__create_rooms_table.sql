CREATE TABLE rooms (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,

                       room_number VARCHAR(255) NOT NULL,
                       room_type VARCHAR(20) NOT NULL,
                       description VARCHAR(1000),

                       price_per_night DECIMAL(10,2) NOT NULL,
                       capacity INT NOT NULL,
                       bed_count INT NOT NULL,

                       hotel_id BIGINT NOT NULL,

                       status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

                       created_at DATETIME NOT NULL,
                       updated_at DATETIME NOT NULL,

                       CONSTRAINT fk_rooms_hotel
                           FOREIGN KEY (hotel_id)
                               REFERENCES hotels(id)
);