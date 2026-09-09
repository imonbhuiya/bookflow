package com.bookflow.booking.entity;


import com.bookflow.booking.enums.BookingStatus;
import com.bookflow.room.entity.Room;
import com.bookflow.user.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate checkInDate;
    @Column(nullable = false)
    private LocalDate checkOutDate;
    @Column(nullable = false)
    private Integer numberOfGuests;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    protected Booking() {
    }
    public Booking(
            User user,
            Room room,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Integer numberOfGuests,
            BigDecimal totalPrice) {

        this.user = user;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;

    }
    public Room getRoom() {
        return room;
    }
    public Long getId() {
        return id;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void confirm() {
        this.status = BookingStatus.CONFIRMED;
    }
    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }
    public void complete() {
        this.status = BookingStatus.COMPLETED;
    }
    public void modify(
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Integer numberOfGuests,
            BigDecimal totalPrice) {

        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
    }
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    public User getUser() {
        return user;
    }

}
