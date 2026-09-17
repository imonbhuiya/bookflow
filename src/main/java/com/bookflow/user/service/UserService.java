package com.bookflow.user.service;

import com.bookflow.exception.ResourceNotFoundException;
import com.bookflow.user.dto.UserCreateRequest;
import com.bookflow.user.dto.UserResponse;
import com.bookflow.user.dto.UserUpdateRequest;
import com.bookflow.user.entity.User;
import com.bookflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        return mapToResponse(user);
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

        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

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

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        userRepository.delete(user);
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