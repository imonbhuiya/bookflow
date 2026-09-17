package com.bookflow.booking.service;

import com.bookflow.booking.dto.BookingCreateRequest;
import com.bookflow.booking.dto.BookingResponse;
import com.bookflow.booking.dto.BookingUpdateRequest;
import com.bookflow.booking.entity.Booking;
import com.bookflow.booking.repository.BookingRepository;
import com.bookflow.room.entity.Room;
import com.bookflow.room.repository.RoomRepository;
import com.bookflow.user.entity.User;
import com.bookflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            RoomRepository roomRepository) {

        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Optional<BookingResponse> getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(this::mapToResponse);
    }

    public BookingResponse createBooking(BookingCreateRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + request.getUserId()
                        )
                );

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Room not found with id: " + request.getRoomId()
                        )
                );

        long numberOfNights = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        BigDecimal totalPrice = room.getPricePerNight()
                .multiply(BigDecimal.valueOf(numberOfNights));

        Booking booking = new Booking(
                user,
                room,
                request.getCheckInDate(),
                request.getCheckOutDate(),
                request.getNumberOfGuests(),
                totalPrice
        );

        Booking savedBooking = bookingRepository.save(booking);

        return mapToResponse(savedBooking);
    }

    public BookingResponse updateBooking(
            Long id,
            BookingUpdateRequest request) {

        Optional<Booking> existingBooking = bookingRepository.findById(id);

        if (existingBooking.isPresent()) {

            Booking bookingToUpdate = existingBooking.get();

            long numberOfNights = ChronoUnit.DAYS.between(
                    request.getCheckInDate(),
                    request.getCheckOutDate()
            );

            BigDecimal totalPrice = bookingToUpdate
                    .getRoom()
                    .getPricePerNight()
                    .multiply(BigDecimal.valueOf(numberOfNights));

            bookingToUpdate.modify(
                    request.getCheckInDate(),
                    request.getCheckOutDate(),
                    request.getNumberOfGuests(),
                    totalPrice
            );

            Booking updatedBooking = bookingRepository.save(bookingToUpdate);

            return mapToResponse(updatedBooking);
        }

        throw new RuntimeException("Booking not found with id: " + id);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    private BookingResponse mapToResponse(Booking booking) {

        return new BookingResponse(
                booking.getId(),
                booking.getUser().getId(),
                booking.getRoom().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getNumberOfGuests(),
                booking.getTotalPrice(),
                booking.getStatus(),
                booking.getCreatedAt(),
                booking.getUpdatedAt()
        );
    }
}