package com.bookflow.room.dto;

import com.bookflow.room.enums.RoomType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class RoomUpdateRequest {

    @NotBlank
    private String roomNumber;

    @NotNull
    private RoomType roomType;

    private String description;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal pricePerNight;

    @NotNull
    @Min(1)
    private Integer capacity;

    @NotNull
    @Min(1)
    private Integer bedCount;

    public RoomUpdateRequest() {
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getBedCount() {
        return bedCount;
    }
}