package com.bookflow.room.service;

import com.bookflow.room.entity.Room;
import com.bookflow.room.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms(){

        return roomRepository.findAll();
    }
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
    public Room updateRoom(Long id, Room room) {

        Optional<Room> existingRoom = roomRepository.findById(id);

        if (existingRoom.isPresent()) {

            Room roomToUpdate = existingRoom.get();

            roomToUpdate.setRoomNumber(room.getRoomNumber());
            roomToUpdate.setRoomType(room.getRoomType());
            roomToUpdate.setDescription(room.getDescription());
            roomToUpdate.setPricePerNight(room.getPricePerNight());
            roomToUpdate.setCapacity(room.getCapacity());
            roomToUpdate.setBedCount(room.getBedCount());

            return roomRepository.save(roomToUpdate);
        }

        throw new RuntimeException("Room not found with id: " + id);
    }




}