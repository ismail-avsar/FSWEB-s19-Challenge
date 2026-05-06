package com.workintech.twitterapi.dto;

import java.time.LocalDateTime;

public record TweetResponse(
        Long id,
        String content,
        Long userId,
        String username,
        long likeCount,
        long commentCount,
        long retweetCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
