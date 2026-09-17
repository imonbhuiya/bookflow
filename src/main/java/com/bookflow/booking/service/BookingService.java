package com.bookflow.booking.service;

import com.bookflow.booking.entity.Booking;
import com.bookflow.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Long id, Booking booking) {

        Optional<Booking> existingBooking = bookingRepository.findById(id);

        if (existingBooking.isPresent()) {

            Booking bookingToUpdate = existingBooking.get();

            bookingToUpdate.modify(
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    booking.getNumberOfGuests(),
                    booking.getTotalPrice()
            );

            return bookingRepository.save(bookingToUpdate);
        }

        throw new RuntimeException("Booking not found with id: " + id);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}