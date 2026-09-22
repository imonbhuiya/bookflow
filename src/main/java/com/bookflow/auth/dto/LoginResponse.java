package com.bookflow.auth.dto;

import com.bookflow.user.dto.UserResponse;

public record LoginResponse(
        String token,
        UserResponse user
) {
}