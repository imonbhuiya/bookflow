package com.bookflow.room.service;

import com.bookflow.exception.ResourceNotFoundException;
import com.bookflow.hotel.entity.Hotel;
import com.bookflow.hotel.repository.HotelRepository;
import com.bookflow.room.dto.RoomCreateRequest;
import com.bookflow.room.dto.RoomResponse;
import com.bookflow.room.dto.RoomUpdateRequest;
import com.bookflow.room.entity.Room;
import com.bookflow.room.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    public RoomService(
            RoomRepository roomRepository,
            HotelRepository hotelRepository) {

        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RoomResponse getRoomById(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room not found with id: " + id
                        )
                );

        return mapToResponse(room);
    }

    public RoomResponse createRoom(RoomCreateRequest request) {

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Hotel not found with id: " + request.getHotelId()
                        )
                );

        Room room = new Room(
                hotel,
                request.getRoomNumber(),
                request.getRoomType(),
                request.getDescription(),
                request.getPricePerNight(),
                request.getCapacity(),
                request.getBedCount()
        );

        Room savedRoom = roomRepository.save(room);

        return mapToResponse(savedRoom);
    }

    public RoomResponse updateRoom(Long id, RoomUpdateRequest request) {

        Room roomToUpdate = roomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room not found with id: " + id
                        )
                );

        roomToUpdate.setRoomNumber(request.getRoomNumber());
        roomToUpdate.setRoomType(request.getRoomType());
        roomToUpdate.setDescription(request.getDescription());
        roomToUpdate.setPricePerNight(request.getPricePerNight());
        roomToUpdate.setCapacity(request.getCapacity());
        roomToUpdate.setBedCount(request.getBedCount());

        Room updatedRoom = roomRepository.save(roomToUpdate);

        return mapToResponse(updatedRoom);
    }

    public void deleteRoom(Long id) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Room not found with id: " + id
                        )
                );

        roomRepository.delete(room);
    }

    private RoomResponse mapToResponse(Room room) {

        return new RoomResponse(
                room.getId(),
                room.getHotel().getId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getDescription(),
                room.getPricePerNight(),
                room.getCapacity(),
                room.getBedCount(),
                room.getStatus(),
                room.getCreatedAt(),
                room.getUpdatedAt()
        );
    }
}