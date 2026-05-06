package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.LikeRequest;
import com.workintech.twitterapi.dto.MessageResponse;
import com.workintech.twitterapi.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like")
    public MessageResponse like(@Valid @RequestBody LikeRequest request) {
        likeService.like(request);
        return new MessageResponse("Tweet liked");
    }

    @PostMapping("/dislike")
    public MessageResponse dislike(@Valid @RequestBody LikeRequest request) {
        likeService.dislike(request);
        return new MessageResponse("Tweet like removed");
    }
}
