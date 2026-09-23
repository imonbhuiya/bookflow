package com.bookflow.user.controller;

import com.bookflow.user.dto.UserCreateRequest;
import com.bookflow.user.dto.UserResponse;
import com.bookflow.user.dto.UserUpdateRequest;
import com.bookflow.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(
        name = "Users",
        description = "User management endpoints for administrators"
)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get all users",
            description = "Returns all users. ADMIN role is required."
    )
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns a user by its ID. ADMIN role is required."
    )
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(
            summary = "Create a user",
            description = "Creates a new user. ADMIN role is required."
    )
    @PostMapping
    public UserResponse createUser(
            @Valid @RequestBody UserCreateRequest request) {

        return userService.createUser(request);
    }

    @Operation(
            summary = "Update a user",
            description = "Updates an existing user by its ID. ADMIN role is required."
    )
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        return userService.updateUser(id, request);
    }

    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by its ID. ADMIN role is required."
    )
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}