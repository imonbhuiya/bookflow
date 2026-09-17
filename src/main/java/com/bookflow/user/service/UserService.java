package com.bookflow.user.service;

import com.bookflow.user.dto.UserCreateRequest;
import com.bookflow.user.dto.UserUpdateRequest;
import com.bookflow.user.dto.UserResponse;
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

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToResponse);
    }

    public UserResponse createUser(UserCreateRequest request) {

        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                request.getDateOfBirth(),
                request.getGender(),
                request.getStreet(),
                request.getCity(),
                request.getPostalCode(),
                request.getCountry()
        );

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {

        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {

            User userToUpdate = existingUser.get();

            userToUpdate.setFirstName(request.getFirstName());
            userToUpdate.setLastName(request.getLastName());
            userToUpdate.setPhone(request.getPhone());
            userToUpdate.setDateOfBirth(request.getDateOfBirth());
            userToUpdate.setGender(request.getGender());
            userToUpdate.setStreet(request.getStreet());
            userToUpdate.setCity(request.getCity());
            userToUpdate.setPostalCode(request.getPostalCode());
            userToUpdate.setCountry(request.getCountry());

            User updatedUser = userRepository.save(userToUpdate);

            return mapToResponse(updatedUser);
        }

        throw new RuntimeException("User not found with id: " + id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserResponse mapToResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getStreet(),
                user.getCity(),
                user.getPostalCode(),
                user.getCountry(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}