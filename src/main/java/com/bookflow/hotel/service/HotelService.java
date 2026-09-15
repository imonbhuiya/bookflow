package com.bookflow.hotel.service;

import com.bookflow.hotel.entity.Hotel;
import java.util.List;
import com.bookflow.hotel.repository.HotelRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
    public Optional<Hotel> getHotelById(Long id) {

        return hotelRepository.findById(id);
    }
    public Hotel createHotel(Hotel hotel) {

        return hotelRepository.save(hotel);
    }
    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);

    }
    public Hotel updateHotel(Long id, Hotel hotel) {

        Optional<Hotel> existingHotel = hotelRepository.findById(id);

        if (existingHotel.isPresent()) {
            Hotel hotelToUpdate = existingHotel.get();

            hotelToUpdate.setName(hotel.getName());
            hotelToUpdate.setDescription(hotel.getDescription());
            hotelToUpdate.setStreet(hotel.getStreet());
            hotelToUpdate.setCity(hotel.getCity());
            hotelToUpdate.setPostalCode(hotel.getPostalCode());
            hotelToUpdate.setCountry(hotel.getCountry());
            hotelToUpdate.setPhone(hotel.getPhone());
            hotelToUpdate.setEmail(hotel.getEmail());
            hotelToUpdate.setStarRating(hotel.getStarRating());
            hotelToUpdate.setCheckInTime(hotel.getCheckInTime());
            hotelToUpdate.setCheckOutTime(hotel.getCheckOutTime());

            return hotelRepository.save(hotelToUpdate);
        }
        throw new RuntimeException("Hotel not found with id: " + id);

    }
}
