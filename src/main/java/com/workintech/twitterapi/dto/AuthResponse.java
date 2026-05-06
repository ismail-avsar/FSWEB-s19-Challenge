package com.workintech.twitterapi.dto;

public record AuthResponse(
        String token,
        Long userId,
        String username
) {
}
