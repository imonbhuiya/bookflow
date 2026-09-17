package com.bookflow.room.service;

import com.bookflow.hotel.entity.Hotel;
import com.bookflow.hotel.repository.HotelRepository;
import com.bookflow.room.dto.RoomCreateRequest;
import com.bookflow.room.dto.RoomResponse;
import com.bookflow.room.dto.RoomUpdateRequest;
import com.bookflow.room.entity.Room;
import com.bookflow.room.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<RoomResponse> getRoomById(Long id) {
        return roomRepository.findById(id)
                .map(this::mapToResponse);
    }

    public RoomResponse createRoom(RoomCreateRequest request) {

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() ->
                        new RuntimeException(
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

        Optional<Room> existingRoom = roomRepository.findById(id);

        if (existingRoom.isPresent()) {

            Room roomToUpdate = existingRoom.get();

            roomToUpdate.setRoomNumber(request.getRoomNumber());
            roomToUpdate.setRoomType(request.getRoomType());
            roomToUpdate.setDescription(request.getDescription());
            roomToUpdate.setPricePerNight(request.getPricePerNight());
            roomToUpdate.setCapacity(request.getCapacity());
            roomToUpdate.setBedCount(request.getBedCount());

            Room updatedRoom = roomRepository.save(roomToUpdate);

            return mapToResponse(updatedRoom);
        }

        throw new RuntimeException("Room not found with id: " + id);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
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