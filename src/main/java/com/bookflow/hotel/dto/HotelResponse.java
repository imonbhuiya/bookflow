package com.bookflow.hotel.dto;

import com.bookflow.hotel.enums.HotelStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class HotelResponse {

    private Long id;
    private String name;
    private String description;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String phone;
    private String email;
    private Integer starRating;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private HotelStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public HotelResponse(
            Long id,
            String name,
            String description,
            String street,
            String city,
            String postalCode,
            String country,
            String phone,
            String email,
            Integer starRating,
            LocalTime checkInTime,
            LocalTime checkOutTime,
            HotelStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.starRating = starRating;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Integer getStarRating() {
        return starRating;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public HotelStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}