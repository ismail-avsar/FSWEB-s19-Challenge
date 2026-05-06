package com.workintech.twitterapi.dto;

import jakarta.validation.constraints.NotNull;

public record LikeRequest(
        @NotNull Long tweetId
) {
}
