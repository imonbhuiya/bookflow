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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private BookingService bookingService;


    // =========================================================
    // GET ALL BOOKINGS
    // =========================================================

    @Test
    void getAllBookings_shouldReturnAllBookings() {

        User user = mock(User.class);
        Room room = mock(Room.class);
        Booking booking = mock(Booking.class);

        when(user.getId()).thenReturn(1L);
        when(room.getId()).thenReturn(10L);

        when(booking.getId()).thenReturn(100L);
        when(booking.getUser()).thenReturn(user);
        when(booking.getRoom()).thenReturn(room);
        when(booking.getCheckInDate())
                .thenReturn(LocalDate.of(2026, 10, 10));
        when(booking.getCheckOutDate())
                .thenReturn(LocalDate.of(2026, 10, 12));
        when(booking.getNumberOfGuests()).thenReturn(2);
        when(booking.getTotalPrice())
                .thenReturn(new BigDecimal("240.00"));
        when(booking.getStatus()).thenReturn(BookingStatus.PENDING);

        when(bookingRepository.findAll())
                .thenReturn(List.of(booking));

        List<BookingResponse> result =
                bookingService.getAllBookings();

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getId());
        assertEquals(1L, result.get(0).getUserId());
        assertEquals(10L, result.get(0).getRoomId());

        verify(bookingRepository).findAll();
    }


    // =========================================================
    // GET MY BOOKINGS
    // =========================================================

    @Test
    void getMyBookings_shouldReturnBookingsForUser() {

        String email = "test@bookflow.com";

        User user = mock(User.class);
        Room room = mock(Room.class);
        Booking booking = mock(Booking.class);

        when(user.getId()).thenReturn(1L);

        when(room.getId()).thenReturn(10L);

        when(booking.getId()).thenReturn(100L);
        when(booking.getUser()).thenReturn(user);
        when(booking.getRoom()).thenReturn(room);
        when(booking.getCheckInDate())
                .thenReturn(LocalDate.of(2026, 10, 10));
        when(booking.getCheckOutDate())
                .thenReturn(LocalDate.of(2026, 10, 12));
        when(booking.getNumberOfGuests()).thenReturn(2);
        when(booking.getTotalPrice())
                .thenReturn(new BigDecimal("240.00"));
        when(booking.getStatus()).thenReturn(BookingStatus.PENDING);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(bookingRepository.findByUserId(1L))
                .thenReturn(List.of(booking));

        List<BookingResponse> result =
                bookingService.getMyBookings(email);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());

        verify(bookingRepository).findByUserId(1L);
    }


    @Test
    void getMyBookings_shouldThrowExceptionWhenUserDoesNotExist() {

        String email = "missing@bookflow.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.getMyBookings(email)
        );

        assertEquals(
                "User not found with email: " + email,
                exception.getMessage()
        );

        verify(bookingRepository, never())
                .findByUserId(anyLong());
    }


    // =========================================================
    // GET BOOKING BY ID
    // =========================================================

    @Test
    void getBookingById_shouldReturnBookingWhenUserIsOwner() {

        String email = "owner@bookflow.com";

        User user = mock(User.class);
        Room room = mock(Room.class);
        Booking booking = mock(Booking.class);

        when(user.getId()).thenReturn(1L);
        when(user.getRole()).thenReturn(Role.USER);

        when(room.getId()).thenReturn(10L);

        when(booking.getId()).thenReturn(100L);
        when(booking.getUser()).thenReturn(user);
        when(booking.getRoom()).thenReturn(room);
        when(booking.getCheckInDate())
                .thenReturn(LocalDate.of(2026, 10, 10));
        when(booking.getCheckOutDate())
                .thenReturn(LocalDate.of(2026, 10, 12));
        when(booking.getNumberOfGuests()).thenReturn(2);
        when(booking.getTotalPrice())
                .thenReturn(new BigDecimal("240.00"));
        when(booking.getStatus()).thenReturn(BookingStatus.PENDING);

        when(bookingRepository.findById(100L))
                .thenReturn(Optional.of(booking));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        BookingResponse response =
                bookingService.getBookingById(100L, email);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(1L, response.getUserId());
    }


    @Test
    void getBookingById_shouldReturnBookingWhenUserIsAdmin() {

        String email = "admin@bookflow.com";

        User admin = mock(User.class);
        User owner = mock(User.class);
        Room room = mock(Room.class);
        Booking booking = mock(Booking.class);

        when(admin.getRole()).thenReturn(Role.ADMIN);
        when(admin.getId()).thenReturn(99L);

        when(owner.getId()).thenReturn(1L);

        when(room.getId()).thenReturn(10L);

        when(booking.getId()).thenReturn(100L);
        when(booking.getUser()).thenReturn(owner);
        when(booking.getRoom()).thenReturn(room);
        when(booking.getCheckInDate())
                .thenReturn(LocalDate.of(2026, 10, 10));
        when(booking.getCheckOutDate())
                .thenReturn(LocalDate.of(2026, 10, 12));
        when(booking.getNumberOfGuests()).thenReturn(2);
        when(booking.getTotalPrice())
                .thenReturn(new BigDecimal("240.00"));
        when(booking.getStatus()).thenReturn(BookingStatus.PENDING);

        when(bookingRepository.findById(100L))
                .thenReturn(Optional.of(booking));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(admin));

        BookingResponse response =
                bookingService.getBookingById(100L, email);

        assertNotNull(response);
        assertEquals(100L, response.getId());
    }


    @Test
    void getBookingById_shouldRejectUserWhoIsNotOwner() {

        String email = "other@bookflow.com";

        User owner = mock(User.class);
        User otherUser = mock(User.class);
        Booking booking = mock(Booking.class);

        when(owner.getId()).thenReturn(1L);

        when(otherUser.getId()).thenReturn(2L);
        when(otherUser.getRole()).thenReturn(Role.USER);

        when(booking.getUser()).thenReturn(owner);

        when(bookingRepository.findById(100L))
                .thenReturn(Optional.of(booking));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(otherUser));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> bookingService.getBookingById(100L, email)
        );

        assertEquals(
                "You are not allowed to access this booking",
                exception.getMessage()
        );
    }


    @Test
    void getBookingById_shouldThrowExceptionWhenBookingDoesNotExist() {

        when(bookingRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.getBookingById(
                        999L,
                        "test@bookflow.com"
                )
        );

        assertEquals(
                "Booking not found with id: 999",
                exception.getMessage()
        );
    }


    // =========================================================
    // CREATE BOOKING
    // =========================================================

    @Test
    void createBooking_shouldRejectInvalidDates() {

        String email = "test@bookflow.com";

        BookingCreateRequest request =
                mock(BookingCreateRequest.class);

        User user = mock(User.class);
        Room room = mock(Room.class);

        when(request.getRoomId()).thenReturn(1L);

        when(request.getCheckInDate())
                .thenReturn(LocalDate.of(2026, 10, 10));

        when(request.getCheckOutDate())
                .thenReturn(LocalDate.of(2026, 10, 9));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        InvalidBookingException exception = assertThrows(
                InvalidBookingException.class,
                () -> bookingService.createBooking(request, email)
        );

        assertEquals(
                "Check-out date must be after check-in date",
                exception.getMessage()
        );

        verify(bookingRepository, never()).save(any());
    }


    @Test
    void createBooking_shouldRejectWhenGuestsExceedRoomCapacity() {

        String email = "test@bookflow.com";

        BookingCreateRequest request =
                mock(BookingCreateRequest.class);

        User user = mock(User.class);
        Room room = mock(Room.class);

        when(request.getRoomId()).thenReturn(1L);

        when(request.getCheckInDate())
                .thenReturn(LocalDate.of(2026, 10, 10));

        when(request.getCheckOutDate())
                .thenReturn(LocalDate.of(2026, 10, 12));

        when(request.getNumberOfGuests()).thenReturn(3);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        when(room.getCapacity()).thenReturn(2);

        InvalidBookingException exception = assertThrows(
                InvalidBookingException.class,
                () -> bookingService.createBooking(request, email)
        );

        assertEquals(
                "Number of guests exceeds room capacity",
                exception.getMessage()
        );

        verify(bookingRepository, never()).save(any());
    }


    @Test
    void createBooking_shouldRejectWhenRoomIsNotActive() {

        String email = "test@bookflow.com";

        BookingCreateRequest request =
                mock(BookingCreateRequest.class);

        User user = mock(User.class);
        Room room = mock(Room.class);

        when(request.getRoomId()).thenReturn(1L);

        when(request.getCheckInDate())
                .thenReturn(LocalDate.of(2026, 10, 10));

        when(request.getCheckOutDate())
                .thenReturn(LocalDate.of(2026, 10, 12));

        when(request.getNumberOfGuests()).thenReturn(2);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        when(room.getCapacity()).thenReturn(2);
        when(room.getStatus())
                .thenReturn(RoomStatus.MAINTENANCE);

        InvalidBookingException exception = assertThrows(
                InvalidBookingException.class,
                () -> bookingService.createBooking(request, email)
        );

        assertEquals(
                "Room is not available for booking",
                exception.getMessage()
        );

        verify(bookingRepository, never()).save(any());
    }


    @Test
    void createBooking_shouldRejectWhenRoomIsAlreadyBooked() {

        String email = "test@bookflow.com";

        LocalDate checkIn =
                LocalDate.of(2026, 10, 10);

        LocalDate checkOut =
                LocalDate.of(2026, 10, 12);

        BookingCreateRequest request =
                mock(BookingCreateRequest.class);

        User user = mock(User.class);
        Room room = mock(Room.class);

        when(request.getRoomId()).thenReturn(1L);
        when(request.getCheckInDate()).thenReturn(checkIn);
        when(request.getCheckOutDate()).thenReturn(checkOut);
        when(request.getNumberOfGuests()).thenReturn(2);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        when(room.getId()).thenReturn(1L);
        when(room.getCapacity()).thenReturn(2);
        when(room.getStatus()).thenReturn(RoomStatus.ACTIVE);

        when(
                bookingRepository
                        .existsByRoomIdAndStatusNotAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                                1L,
                                BookingStatus.CANCELLED,
                                checkOut,
                                checkIn
                        )
        ).thenReturn(true);

        InvalidBookingException exception = assertThrows(
                InvalidBookingException.class,
                () -> bookingService.createBooking(request, email)
        );

        assertEquals(
                "Room is already booked for the selected dates",
                exception.getMessage()
        );

        verify(bookingRepository, never()).save(any());
    }


    @Test
    void createBooking_shouldThrowExceptionWhenUserDoesNotExist() {

        String email = "missing@bookflow.com";

        BookingCreateRequest request =
                mock(BookingCreateRequest.class);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.createBooking(request, email)
        );

        assertEquals(
                "User not found with email: " + email,
                exception.getMessage()
        );

        verify(roomRepository, never())
                .findById(anyLong());

        verify(bookingRepository, never())
                .save(any());
    }


    @Test
    void createBooking_shouldThrowExceptionWhenRoomDoesNotExist() {

        String email = "test@bookflow.com";

        BookingCreateRequest request =
                mock(BookingCreateRequest.class);

        User user = mock(User.class);

        when(request.getRoomId()).thenReturn(99L);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(roomRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.createBooking(request, email)
        );

        assertEquals(
                "Room not found with id: 99",
                exception.getMessage()
        );

        verify(bookingRepository, never())
                .save(any());
    }


    @Test
    void createBooking_shouldCreateBookingSuccessfully() {

        String email = "test@bookflow.com";

        LocalDate checkIn =
                LocalDate.of(2026, 10, 10);

        LocalDate checkOut =
                LocalDate.of(2026, 10, 12);

        BookingCreateRequest request =
                mock(BookingCreateRequest.class);

        User user = mock(User.class);
        Room room = mock(Room.class);

        when(request.getRoomId()).thenReturn(1L);
        when(request.getCheckInDate()).thenReturn(checkIn);
        when(request.getCheckOutDate()).thenReturn(checkOut);
        when(request.getNumberOfGuests()).thenReturn(2);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(roomRepository.findById(1L))
                .thenReturn(Optional.of(room));

        when(user.getId()).thenReturn(1L);

        when(room.getId()).thenReturn(1L);
        when(room.getCapacity()).thenReturn(2);
        when(room.getStatus()).thenReturn(RoomStatus.ACTIVE);

        when(room.getPricePerNight())
                .thenReturn(new BigDecimal("120.00"));

        when(
                bookingRepository
                        .existsByRoomIdAndStatusNotAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                                1L,
                                BookingStatus.CANCELLED,
                                checkOut,
                                checkIn
                        )
        ).thenReturn(false);

        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        BookingResponse response =
                bookingService.createBooking(request, email);

        assertNotNull(response);

        assertEquals(1L, response.getUserId());
        assertEquals(1L, response.getRoomId());

        assertEquals(
                checkIn,
                response.getCheckInDate()
        );

        assertEquals(
                checkOut,
                response.getCheckOutDate()
        );

        assertEquals(
                2,
                response.getNumberOfGuests()
        );

        assertEquals(
                0,
                new BigDecimal("240.00")
                        .compareTo(response.getTotalPrice())
        );

        assertEquals(
                BookingStatus.PENDING,
                response.getStatus()
        );

        verify(bookingRepository)
                .save(any(Booking.class));
    }


    // =========================================================
    // UPDATE BOOKING
    // =========================================================

    @Test
    void updateBooking_shouldUpdateBookingSuccessfullyForOwner() {

        String email = "owner@bookflow.com";

        LocalDate newCheckIn =
                LocalDate.of(2026, 11, 10);

        LocalDate newCheckOut =
                LocalDate.of(2026, 11, 13);

        BookingUpdateRequest request =
                mock(BookingUpdateRequest.class);

        Booking booking = mock(Booking.class);
        User user = mock(User.class);
        Room room = mock(Room.class);

        when(request.getCheckInDate())
                .thenReturn(newCheckIn);

        when(request.getCheckOutDate())
                .thenReturn(newCheckOut);

        when(request.getNumberOfGuests())
                .thenReturn(2);

        when(bookingRepository.findById(100L))
                .thenReturn(Optional.of(booking));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(user.getId()).thenReturn(1L);
        when(user.getRole()).thenReturn(Role.USER);

        when(booking.getId()).thenReturn(100L);
        when(booking.getUser()).thenReturn(user);
        when(booking.getRoom()).thenReturn(room);
        when(booking.getStatus())
                .thenReturn(BookingStatus.PENDING);

        when(room.getId()).thenReturn(10L);
        when(room.getCapacity()).thenReturn(2);
        when(room.getStatus()).thenReturn(RoomStatus.ACTIVE);

        when(room.getPricePerNight())
                .thenReturn(new BigDecimal("120.00"));

        when(
                bookingRepository
                        .existsByRoomIdAndIdNotAndStatusNotAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                                10L,
                                100L,
                                BookingStatus.CANCELLED,
                                newCheckOut,
                                newCheckIn
                        )
        ).thenReturn(false);

        when(bookingRepository.save(booking))
                .thenReturn(booking);

        when(booking.getCheckInDate())
                .thenReturn(newCheckIn);

        when(booking.getCheckOutDate())
                .thenReturn(newCheckOut);

        when(booking.getNumberOfGuests())
                .thenReturn(2);

        when(booking.getTotalPrice())
                .thenReturn(new BigDecimal("360.00"));

        BookingResponse response =
                bookingService.updateBooking(
                        100L,
                        request,
                        email
                );

        assertNotNull(response);

        assertEquals(
                newCheckIn,
                response.getCheckInDate()
        );

        assertEquals(
                newCheckOut,
                response.getCheckOutDate()
        );

        assertEquals(
                0,
                new BigDecimal("360.00")
                        .compareTo(response.getTotalPrice())
        );

        verify(booking).modify(
                newCheckIn,
                newCheckOut,
                2,
                new BigDecimal("360.00")
        );

        verify(bookingRepository).save(booking);
    }


    @Test
    void updateBooking_shouldRejectUserWhoIsNotOwner() {

        String email = "other@bookflow.com";

        BookingUpdateRequest request =
                mock(BookingUpdateRequest.class);

        Booking booking = mock(Booking.class);
        User owner = mock(User.class);
        User otherUser = mock(User.class);

        when(owner.getId()).thenReturn(1L);

        when(otherUser.getId()).thenReturn(2L);
        when(otherUser.getRole()).thenReturn(Role.USER);

        when(booking.getUser()).thenReturn(owner);

        when(bookingRepository.findById(100L))
                .thenReturn(Optional.of(booking));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(otherUser));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> bookingService.updateBooking(
                        100L,
                        request,
                        email
                )
        );

        assertEquals(
                "You are not allowed to update this booking",
                exception.getMessage()
        );

        verify(bookingRepository, never()).save(any());
    }


    @Test
    void updateBooking_shouldRejectCancelledBooking() {

        String email = "owner@bookflow.com";

        BookingUpdateRequest request =
                mock(BookingUpdateRequest.class);

        Booking booking = mock(Booking.class);
        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getRole()).thenReturn(Role.USER);

        when(booking.getUser()).thenReturn(user);

        when(booking.getStatus())
                .thenReturn(BookingStatus.CANCELLED);

        when(bookingRepository.findById(100L))
                .thenReturn(Optional.of(booking));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        InvalidBookingException exception = assertThrows(
                InvalidBookingException.class,
                () -> bookingService.updateBooking(
                        100L,
                        request,
                        email
                )
        );

        assertEquals(
                "Cancelled booking cannot be modified",
                exception.getMessage()
        );

        verify(bookingRepository, never()).save(any());
    }


    @Test
    void updateBooking_shouldRejectOverlappingBooking() {

        String email = "owner@bookflow.com";

        LocalDate checkIn =
                LocalDate.of(2026, 12, 10);

        LocalDate checkOut =
                LocalDate.of(2026, 12, 12);

        BookingUpdateRequest request =
                mock(BookingUpdateRequest.class);

        Booking booking = mock(Booking.class);
        User user = mock(User.class);
        Room room = mock(Room.class);

        when(request.getCheckInDate()).thenReturn(checkIn);
        when(request.getCheckOutDate()).thenReturn(checkOut);
        when(request.getNumberOfGuests()).thenReturn(2);

        when(user.getId()).thenReturn(1L);
        when(user.getRole()).thenReturn(Role.USER);

        when(booking.getId()).thenReturn(100L);
        when(booking.getUser()).thenReturn(user);
        when(booking.getRoom()).thenReturn(room);

        when(booking.getStatus())
                .thenReturn(BookingStatus.PENDING);

        when(room.getId()).thenReturn(10L);
        when(room.getCapacity()).thenReturn(2);
        when(room.getStatus()).thenReturn(RoomStatus.ACTIVE);

        when(bookingRepository.findById(100L))
                .thenReturn(Optional.of(booking));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(
                bookingRepository
                        .existsByRoomIdAndIdNotAndStatusNotAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                                10L,
                                100L,
                                BookingStatus.CANCELLED,
                                checkOut,
                                checkIn
                        )
        ).thenReturn(true);

        InvalidBookingException exception = assertThrows(
                InvalidBookingException.class,
                () -> bookingService.updateBooking(
                        100L,
                        request,
                        email
                )
        );

        assertEquals(
                "Room is already booked for the selected dates",
                exception.getMessage()
        );

        verify(bookingRepository, never()).save(any());
    }


    // =========================================================
    // DELETE / CANCEL BOOKING
    // =========================================================

    @Test
    void deleteBooking_shouldCancelBookingSuccessfullyForOwner() {

        String email = "owner@bookflow.com";

        Booking booking = mock(Booking.class);
        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getRole()).thenReturn(Role.USER);

        when(booking.getUser()).thenReturn(user);

        when(booking.getStatus())
                .thenReturn(BookingStatus.PENDING);

        when(bookingRepository.findById(100L))
                .thenReturn(Optional.of(booking));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        bookingService.deleteBooking(100L, email);

        verify(booking).cancel();
        verify(bookingRepository).save(booking);
    }


    @Test
    void deleteBooking_shouldAllowAdminToCancelBooking() {

        String email = "admin@bookflow.com";

        Booking booking = mock(Booking.class);
        User owner = mock(User.class);
        User admin = mock(User.class);

        when(owner.getId()).thenReturn(1L);

        when(admin.getId()).thenReturn(99L);
        when(admin.getRole()).thenReturn(Role.ADMIN);

        when(booking.getUser()).thenReturn(owner);

        when(booking.getStatus())
                .thenReturn(BookingStatus.PENDING);

        when(bookingRepository.findById(100L))
                .thenReturn(Optional.of(booking));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(admin));

        bookingService.deleteBooking(100L, email);

        verify(booking).cancel();
        verify(bookingRepository).save(booking);
    }


    @Test
    void deleteBooking_shouldRejectUserWhoIsNotOwner() {

        String email = "other@bookflow.com";

        Booking booking = mock(Booking.class);
        User owner = mock(User.class);
        User otherUser = mock(User.class);

        when(owner.getId()).thenReturn(1L);

        when(otherUser.getId()).thenReturn(2L);
        when(otherUser.getRole()).thenReturn(Role.USER);

        when(booking.getUser()).thenReturn(owner);

        when(bookingRepository.findById(100L))
                .thenReturn(Optional.of(booking));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(otherUser));

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> bookingService.deleteBooking(
                        100L,
                        email
                )
        );

        assertEquals(
                "You are not allowed to cancel this booking",
                exception.getMessage()
        );

        verify(booking, never()).cancel();
        verify(bookingRepository, never()).save(any());
    }


    @Test
    void deleteBooking_shouldRejectAlreadyCancelledBooking() {

        String email = "owner@bookflow.com";

        Booking booking = mock(Booking.class);
        User user = mock(User.class);

        when(user.getId()).thenReturn(1L);
        when(user.getRole()).thenReturn(Role.USER);

        when(booking.getUser()).thenReturn(user);

        when(booking.getStatus())
                .thenReturn(BookingStatus.CANCELLED);

        when(bookingRepository.findById(100L))
                .thenReturn(Optional.of(booking));

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        InvalidBookingException exception = assertThrows(
                InvalidBookingException.class,
                () -> bookingService.deleteBooking(
                        100L,
                        email
                )
        );

        assertEquals(
                "Booking is already cancelled",
                exception.getMessage()
        );

        verify(booking, never()).cancel();
        verify(bookingRepository, never()).save(any());
    }


    @Test
    void deleteBooking_shouldThrowExceptionWhenBookingDoesNotExist() {

        when(bookingRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> bookingService.deleteBooking(
                        999L,
                        "test@bookflow.com"
                )
        );

        assertEquals(
                "Booking not found with id: 999",
                exception.getMessage()
        );

        verify(bookingRepository, never()).save(any());
    }
}