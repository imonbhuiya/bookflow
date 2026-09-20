CREATE TABLE hotels (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,

                        name VARCHAR(255) NOT NULL,
                        description VARCHAR(255),

                        street VARCHAR(255) NOT NULL,
                        city VARCHAR(255) NOT NULL,
                        postal_code VARCHAR(255) NOT NULL,
                        country VARCHAR(255) NOT NULL,

                        phone VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,

                        star_rating INT,

                        check_in_time TIME NOT NULL,
                        check_out_time TIME NOT NULL,

                        created_at DATETIME NOT NULL,
                        updated_at DATETIME NOT NULL,

                        status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);