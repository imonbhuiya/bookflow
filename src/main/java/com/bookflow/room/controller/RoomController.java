package com.bookflow.room.controller;

import com.bookflow.room.dto.RoomCreateRequest;
import com.bookflow.room.dto.RoomResponse;
import com.bookflow.room.dto.RoomUpdateRequest;
import com.bookflow.room.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Tag(
        name = "Rooms",
        description = "Room management endpoints"
)
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @Operation(
            summary = "Get all rooms",
            description = "Returns all rooms. Authentication is not required.",
            security = {}
    )
    @GetMapping
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    @Operation(
            summary = "Get room by ID",
            description = "Returns a room by its ID. Authentication is not required.",
            security = {}
    )
    @GetMapping("/{id}")
    public RoomResponse getRoomById(@PathVariable Long id) {
        return roomService.getRoomById(id);
    }

    @Operation(
            summary = "Create a room",
            description = "Creates a new room. ADMIN role is required."
    )
    @PostMapping
    public RoomResponse createRoom(
            @Valid @RequestBody RoomCreateRequest request) {

        return roomService.createRoom(request);
    }

    @Operation(
            summary = "Update a room",
            description = "Updates an existing room by its ID. ADMIN role is required."
    )
    @PutMapping("/{id}")
    public RoomResponse updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomUpdateRequest request) {

        return roomService.updateRoom(id, request);
    }

    @Operation(
            summary = "Delete a room",
            description = "Deletes a room by its ID. ADMIN role is required."
    )
    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }
}