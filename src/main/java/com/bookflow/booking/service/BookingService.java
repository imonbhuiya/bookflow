package com.bookflow.booking.service;

import com.bookflow.booking.dto.BookingCreateRequest;
import com.bookflow.booking.dto.BookingResponse;
import com.bookflow.booking.dto.BookingUpdateRequest;
import com.bookflow.booking.entity.Booking;
import com.bookflow.booking.repository.BookingRepository;
import com.bookflow.exception.ResourceNotFoundException;
import com.bookflow.room.entity.Room;
import com.bookflow.room.repository.RoomRepository;
import com.bookflow.user.entity.User;
import com.bookflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    public BookingResponse getBookingById(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with id: " + id
                        )
                );

        return mapToResponse(booking);
    }

    public BookingResponse createBooking(BookingCreateRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + request.getUserId()
                        )
                );

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
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

        Booking bookingToUpdate = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with id: " + id
                        )
                );

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

    public void deleteBooking(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with id: " + id
                        )
                );

        bookingRepository.delete(booking);
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