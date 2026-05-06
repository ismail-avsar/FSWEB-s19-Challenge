package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.MessageResponse;
import com.workintech.twitterapi.dto.TweetCreateRequest;
import com.workintech.twitterapi.dto.TweetResponse;
import com.workintech.twitterapi.dto.TweetUpdateRequest;
import com.workintech.twitterapi.service.TweetService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tweet")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponse create(@Valid @RequestBody TweetCreateRequest request) {
        return tweetService.create(request);
    }

    @GetMapping("/findByUserId")
    public List<TweetResponse> findByUserId(@RequestParam Long userId) {
        return tweetService.findByUserId(userId);
    }

    @GetMapping("/findById")
    public TweetResponse findById(@RequestParam Long id) {
        return tweetService.findById(id);
    }

    @PutMapping("/{id}")
    public TweetResponse update(@PathVariable Long id, @Valid @RequestBody TweetUpdateRequest request) {
        return tweetService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable Long id) {
        tweetService.delete(id);
        return new MessageResponse("Tweet deleted");
    }
}
