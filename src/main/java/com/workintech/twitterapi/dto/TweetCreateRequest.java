package com.workintech.twitterapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TweetCreateRequest(
        @NotBlank @Size(max = 280) String content
) {
}
