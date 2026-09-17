package com.bookflow.hotel.controller;

import com.bookflow.hotel.dto.HotelCreateRequest;
import com.bookflow.hotel.dto.HotelResponse;
import com.bookflow.hotel.dto.HotelUpdateRequest;
import com.bookflow.hotel.service.HotelService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // GET /api/hotels
    @GetMapping
    public List<HotelResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }

    // GET /api/hotels/{id}
    @GetMapping("/{id}")
    public HotelResponse getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    // POST /api/hotels
    @PostMapping
    public HotelResponse createHotel(
            @Valid @RequestBody HotelCreateRequest request) {

        return hotelService.createHotel(request);
    }

    // PUT /api/hotels/{id}
    @PutMapping("/{id}")
    public HotelResponse updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelUpdateRequest request) {

        return hotelService.updateHotel(id, request);
    }

    // DELETE /api/hotels/{id}
    @DeleteMapping("/{id}")
    public void deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
    }
}