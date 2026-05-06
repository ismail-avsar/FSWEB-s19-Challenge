package com.workintech.twitterapi.dto;

import jakarta.validation.constraints.NotNull;

public record RetweetRequest(
        @NotNull Long tweetId
) {
}
