package com.bookflow.room.dto;

import com.bookflow.room.enums.RoomStatus;
import com.bookflow.room.enums.RoomType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RoomResponse {

    private Long id;
    private Long hotelId;
    private String roomNumber;
    private RoomType roomType;
    private String description;
    private BigDecimal pricePerNight;
    private Integer capacity;
    private Integer bedCount;
    private RoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RoomResponse(
            Long id,
            Long hotelId,
            String roomNumber,
            RoomType roomType,
            String description,
            BigDecimal pricePerNight,
            Integer capacity,
            Integer bedCount,
            RoomStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.bedCount = bedCount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getHotelId() {
        return hotelId;
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

    public RoomStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}