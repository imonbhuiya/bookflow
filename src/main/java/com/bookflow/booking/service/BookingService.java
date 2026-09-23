package com.bookflow.booking.service;

import com.bookflow.booking.dto.BookingCreateRequest;
import com.bookflow.booking.dto.BookingResponse;
import com.bookflow.booking.dto.BookingUpdateRequest;
import com.bookflow.booking.entity.Booking;
import com.bookflow.booking.enums.BookingStatus;
import com.bookflow.booking.repository.BookingRepository;
import com.bookflow.exception.InvalidBookingException;
import com.bookflow.exception.ResourceNotFoundException;
import com.bookflow.room.entity.Room;
import com.bookflow.room.enums.RoomStatus;
import com.bookflow.room.repository.RoomRepository;
import com.bookflow.user.entity.User;
import com.bookflow.user.enums.Role;
import com.bookflow.user.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );

        return bookingRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id, String email) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with id: " + id
                        )
                );

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = booking.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException(
                    "You are not allowed to access this booking"
            );
        }

        return mapToResponse(booking);
    }

    @Transactional
    public BookingResponse createBooking(
            BookingCreateRequest request,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room not found with id: " + request.getRoomId()
                        )
                );

        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new InvalidBookingException(
                    "Check-out date must be after check-in date"
            );
        }

        if (request.getNumberOfGuests() > room.getCapacity()) {
            throw new InvalidBookingException(
                    "Number of guests exceeds room capacity"
            );
        }

        if (room.getStatus() != RoomStatus.ACTIVE) {
            throw new InvalidBookingException(
                    "Room is not available for booking"
            );
        }

        boolean roomAlreadyBooked =
                bookingRepository
                        .existsByRoomIdAndStatusNotAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                                room.getId(),
                                BookingStatus.CANCELLED,
                                request.getCheckOutDate(),
                                request.getCheckInDate()
                        );

        if (roomAlreadyBooked) {
            throw new InvalidBookingException(
                    "Room is already booked for the selected dates"
            );
        }

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

    @Transactional
    public BookingResponse updateBooking(
            Long id,
            BookingUpdateRequest request,
            String email) {

        Booking bookingToUpdate = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with id: " + id
                        )
                );

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = bookingToUpdate.getUser()
                .getId()
                .equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException(
                    "You are not allowed to update this booking"
            );
        }

        if (bookingToUpdate.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidBookingException(
                    "Cancelled booking cannot be modified"
            );
        }

        Room room = bookingToUpdate.getRoom();

        if (!request.getCheckOutDate().isAfter(request.getCheckInDate())) {
            throw new InvalidBookingException(
                    "Check-out date must be after check-in date"
            );
        }

        if (request.getNumberOfGuests() > room.getCapacity()) {
            throw new InvalidBookingException(
                    "Number of guests exceeds room capacity"
            );
        }

        if (room.getStatus() != RoomStatus.ACTIVE) {
            throw new InvalidBookingException(
                    "Room is not available for booking"
            );
        }

        boolean roomAlreadyBooked =
                bookingRepository
                        .existsByRoomIdAndIdNotAndStatusNotAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                                room.getId(),
                                bookingToUpdate.getId(),
                                BookingStatus.CANCELLED,
                                request.getCheckOutDate(),
                                request.getCheckInDate()
                        );

        if (roomAlreadyBooked) {
            throw new InvalidBookingException(
                    "Room is already booked for the selected dates"
            );
        }

        long numberOfNights = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate()
        );

        BigDecimal totalPrice = room.getPricePerNight()
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

    @Transactional
    public void deleteBooking(Long id, String email) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with id: " + id
                        )
                );

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = booking.getUser()
                .getId()
                .equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException(
                    "You are not allowed to cancel this booking"
            );
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidBookingException(
                    "Booking is already cancelled"
            );
        }

        booking.cancel();

        bookingRepository.save(booking);
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