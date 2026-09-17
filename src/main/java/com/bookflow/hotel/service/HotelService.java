package com.bookflow.hotel.service;

import com.bookflow.hotel.dto.HotelCreateRequest;
import com.bookflow.hotel.dto.HotelResponse;
import com.bookflow.hotel.dto.HotelUpdateRequest;
import com.bookflow.hotel.entity.Hotel;
import com.bookflow.hotel.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Optional<HotelResponse> getHotelById(Long id) {
        return hotelRepository.findById(id)
                .map(this::mapToResponse);
    }

    public HotelResponse createHotel(HotelCreateRequest request) {

        Hotel hotel = new Hotel(
                request.getName(),
                request.getDescription(),
                request.getStreet(),
                request.getCity(),
                request.getPostalCode(),
                request.getCountry(),
                request.getPhone(),
                request.getEmail(),
                request.getStarRating(),
                request.getCheckInTime(),
                request.getCheckOutTime()
        );

        Hotel savedHotel = hotelRepository.save(hotel);

        return mapToResponse(savedHotel);
    }

    public HotelResponse updateHotel(Long id, HotelUpdateRequest request) {

        Optional<Hotel> existingHotel = hotelRepository.findById(id);

        if (existingHotel.isPresent()) {

            Hotel hotelToUpdate = existingHotel.get();

            hotelToUpdate.setName(request.getName());
            hotelToUpdate.setDescription(request.getDescription());
            hotelToUpdate.setStreet(request.getStreet());
            hotelToUpdate.setCity(request.getCity());
            hotelToUpdate.setPostalCode(request.getPostalCode());
            hotelToUpdate.setCountry(request.getCountry());
            hotelToUpdate.setPhone(request.getPhone());
            hotelToUpdate.setEmail(request.getEmail());
            hotelToUpdate.setStarRating(request.getStarRating());
            hotelToUpdate.setCheckInTime(request.getCheckInTime());
            hotelToUpdate.setCheckOutTime(request.getCheckOutTime());

            Hotel updatedHotel = hotelRepository.save(hotelToUpdate);

            return mapToResponse(updatedHotel);
        }

        throw new RuntimeException("Hotel not found with id: " + id);
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }

    private HotelResponse mapToResponse(Hotel hotel) {

        return new HotelResponse(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                hotel.getStreet(),
                hotel.getCity(),
                hotel.getPostalCode(),
                hotel.getCountry(),
                hotel.getPhone(),
                hotel.getEmail(),
                hotel.getStarRating(),
                hotel.getCheckInTime(),
                hotel.getCheckOutTime(),
                hotel.getStatus(),
                hotel.getCreatedAt(),
                hotel.getUpdatedAt()
        );
    }
}