package com.bookflow.hotel.controller;

import com.bookflow.hotel.dto.HotelCreateRequest;
import com.bookflow.hotel.dto.HotelResponse;
import com.bookflow.hotel.dto.HotelUpdateRequest;
import com.bookflow.hotel.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@Tag(
        name = "Hotels",
        description = "Hotel management endpoints"
)
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Operation(
            summary = "Get all hotels",
            description = "Returns all hotels. Authentication is not required.",
            security = {}
    )
    @GetMapping
    public List<HotelResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @Operation(
            summary = "Get hotel by ID",
            description = "Returns a hotel by its ID. Authentication is not required.",
            security = {}
    )
    @GetMapping("/{id}")
    public HotelResponse getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    @Operation(
            summary = "Create a hotel",
            description = "Creates a new hotel. ADMIN role is required."
    )
    @PostMapping
    public HotelResponse createHotel(
            @Valid @RequestBody HotelCreateRequest request) {

        return hotelService.createHotel(request);
    }

    @Operation(
            summary = "Update a hotel",
            description = "Updates an existing hotel by its ID. ADMIN role is required."
    )
    @PutMapping("/{id}")
    public HotelResponse updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelUpdateRequest request) {

        return hotelService.updateHotel(id, request);
    }

    @Operation(
            summary = "Delete a hotel",
            description = "Deletes a hotel by its ID. ADMIN role is required."
    )
    @DeleteMapping("/{id}")
    public void deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
    }
}