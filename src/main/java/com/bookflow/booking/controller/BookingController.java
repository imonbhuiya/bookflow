package com.bookflow.booking.controller;

import com.bookflow.booking.dto.BookingCreateRequest;
import com.bookflow.booking.dto.BookingResponse;
import com.bookflow.booking.dto.BookingUpdateRequest;
import com.bookflow.booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingResponse> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/me")
    public List<BookingResponse> getMyBookings(Authentication authentication) {

        String email = authentication.getName();

        return bookingService.getMyBookings(email);
    }

    @GetMapping("/{id}")
    public BookingResponse getBookingById(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        return bookingService.getBookingById(id, email);
    }

    @PostMapping
    public BookingResponse createBooking(
            @Valid @RequestBody BookingCreateRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return bookingService.createBooking(request, email);
    }

    @PutMapping("/{id}")
    public BookingResponse updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingUpdateRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        return bookingService.updateBooking(id, request, email);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        bookingService.deleteBooking(id, email);
    }
}