package com.bookflow.booking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class BookingCreateRequest {

    @NotNull
    private Long roomId;

    @NotNull
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;

    @NotNull
    @Min(1)
    private Integer numberOfGuests;

    public BookingCreateRequest() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}