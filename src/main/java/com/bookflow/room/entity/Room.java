package com.bookflow.room.entity;

import com.bookflow.room.enums.RoomStatus;
import com.bookflow.room.enums.RoomType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomType;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer bedCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.ACTIVE;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    // --- JPA CONSTRUCTOR ---

    protected Room() {
    }


    // --- BUSINESS CONSTRUCTOR ---

    public Room(
            String roomNumber,
            RoomType roomType,
            String description,
            BigDecimal pricePerNight,
            Integer capacity,
            Integer bedCount) {

        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.description = description;
        this.pricePerNight = pricePerNight;
        this.capacity = capacity;
        this.bedCount = bedCount;
    }


    // --- GETTERS ---

    public Long getId() {
        return id;
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


    // --- SETTERS ---

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setBedCount(Integer bedCount) {
        this.bedCount = bedCount;
    }


    // --- BUSINESS METHODS ---

    public void activate() {
        this.status = RoomStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = RoomStatus.INACTIVE;
    }

    public void markUnderMaintenance() {
        this.status = RoomStatus.MAINTENANCE;
    }
}