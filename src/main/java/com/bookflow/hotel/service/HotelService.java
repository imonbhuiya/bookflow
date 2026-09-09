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
}
