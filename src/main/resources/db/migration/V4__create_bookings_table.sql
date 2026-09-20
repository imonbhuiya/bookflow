CREATE TABLE bookings (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,

                          check_in_date DATE NOT NULL,
                          check_out_date DATE NOT NULL,
                          number_of_guests INT NOT NULL,

                          total_price DECIMAL(10,2) NOT NULL,

                          status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

                          created_at DATETIME NOT NULL,
                          updated_at DATETIME NOT NULL,

                          room_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,

                          CONSTRAINT fk_bookings_room
                              FOREIGN KEY (room_id)
                                  REFERENCES rooms(id),

                          CONSTRAINT fk_bookings_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id)
);