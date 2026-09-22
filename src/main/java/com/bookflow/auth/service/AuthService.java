package com.bookflow.auth.service;

import com.bookflow.auth.dto.LoginRequest;
import com.bookflow.auth.dto.RegisterRequest;
import com.bookflow.exception.EmailAlreadyExistsException;
import com.bookflow.exception.InvalidCredentialsException;
import com.bookflow.user.dto.UserResponse;
import com.bookflow.user.entity.User;
import com.bookflow.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(
                    "Email is already registered"
            );
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = new User(
                request.firstName(),
                request.lastName(),
                request.email(),
                encodedPassword,
                request.phone(),
                request.dateOfBirth(),
                request.gender(),
                request.street(),
                request.city(),
                request.postalCode(),
                request.country()
        );

        User savedUser = userRepository.save(user);

        return toUserResponse(savedUser);
    }

    public UserResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Invalid email or password"
                ));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        return toUserResponse(user);
    }

    private UserResponse toUserResponse(User user) {

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