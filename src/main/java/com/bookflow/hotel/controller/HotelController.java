package com.bookflow.hotel.controller;

import com.bookflow.hotel.entity.Hotel;
import com.bookflow.hotel.service.HotelService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // GET /api/hotels
    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }

    // GET /api/hotels/{id}
    @GetMapping("/{id}")
    public Optional<Hotel> getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    // POST /api/hotels
    @PostMapping
    public Hotel createHotel(@RequestBody Hotel hotel) {
        return hotelService.createHotel(hotel);
    }

    // PUT /api/hotels/{id}
    @PutMapping("/{id}")
    public Hotel updateHotel(
            @PathVariable Long id,
            @RequestBody Hotel hotel) {

        return hotelService.updateHotel(id, hotel);
    }

    // DELETE /api/hotels/{id}
    @DeleteMapping("/{id}")
    public void deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
    }
}