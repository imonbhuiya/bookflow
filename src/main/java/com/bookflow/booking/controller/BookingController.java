package com.bookflow.booking.controller;

import com.bookflow.booking.dto.BookingCreateRequest;
import com.bookflow.booking.dto.BookingResponse;
import com.bookflow.booking.dto.BookingUpdateRequest;
import com.bookflow.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(
        name = "Bookings",
        description = "Booking management endpoints"
)
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(
            summary = "Get all bookings",
            description = "Returns all bookings. ADMIN role is required."
    )
    @GetMapping
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @Operation(
            summary = "Get my bookings",
            description = "Returns all bookings belonging to the authenticated user."
    )
    @GetMapping("/me")
    public List<BookingResponse> getMyBookings(Authentication authentication) {

        String email = authentication.getName();

        return bookingService.getMyBookings(email);
    }

    @Operation(
            summary = "Get booking by ID",
            description = "Returns a booking by its ID. The authenticated user must own the booking or have the ADMIN role."
    )
    @GetMapping("/{id}")
    public BookingResponse getBookingById(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        return bookingService.getBookingById(id, email);
    }

    @Operation(
            summary = "Create a booking",
            description = "Creates a new booking for the authenticated user."
    )
    @PostMapping
    public BookingResponse createBooking(
            @Valid @RequestBody BookingCreateRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return bookingService.createBooking(request, email);
    }

    @Operation(
            summary = "Update a booking",
            description = "Updates an existing booking. The authenticated user must own the booking or have the ADMIN role."
    )
    @PutMapping("/{id}")
    public BookingResponse updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingUpdateRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return bookingService.updateBooking(id, request, email);
    }

    @Operation(
            summary = "Cancel a booking",
            description = "Cancels an existing booking. The authenticated user must own the booking or have the ADMIN role."
    )
    @DeleteMapping("/{id}")
    public void deleteBooking(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        bookingService.deleteBooking(id, email);
    }
}