package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.MessageResponse;
import com.workintech.twitterapi.dto.RetweetRequest;
import com.workintech.twitterapi.service.RetweetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RetweetController {

    private final RetweetService retweetService;

    @PostMapping("/retweet")
    public MessageResponse retweet(@Valid @RequestBody RetweetRequest request) {
        retweetService.retweet(request);
        return new MessageResponse("Tweet retweeted");
    }

    @DeleteMapping("/retweet/{id}")
    public MessageResponse delete(@PathVariable Long id) {
        retweetService.delete(id);
        return new MessageResponse("Retweet deleted");
    }
}
