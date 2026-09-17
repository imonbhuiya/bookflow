package com.bookflow.user.service;

import com.bookflow.user.entity.User;
import com.bookflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {

        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {

            User userToUpdate = existingUser.get();

            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setPhone(user.getPhone());
            userToUpdate.setDateOfBirth(user.getDateOfBirth());
            userToUpdate.setGender(user.getGender());
            userToUpdate.setStreet(user.getStreet());
            userToUpdate.setCity(user.getCity());
            userToUpdate.setPostalCode(user.getPostalCode());
            userToUpdate.setCountry(user.getCountry());

            return userRepository.save(userToUpdate);
        }

        throw new RuntimeException("User not found with id: " + id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}