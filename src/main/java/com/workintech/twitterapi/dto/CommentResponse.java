package com.workintech.twitterapi.dto;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long tweetId,
        Long userId,
        String username,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
