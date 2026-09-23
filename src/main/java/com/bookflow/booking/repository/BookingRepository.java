package com.bookflow.booking.repository;

import com.bookflow.booking.entity.Booking;
import com.bookflow.booking.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    boolean existsByRoomIdAndStatusNotAndCheckInDateLessThanAndCheckOutDateGreaterThan(
            Long roomId,
            BookingStatus status,
            LocalDate requestedCheckOut,
            LocalDate requestedCheckIn
    );

    boolean existsByRoomIdAndIdNotAndStatusNotAndCheckInDateLessThanAndCheckOutDateGreaterThan(
            Long roomId,
            Long bookingId,
            BookingStatus status,
            LocalDate requestedCheckOut,
            LocalDate requestedCheckIn
    );
}