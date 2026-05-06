package com.workintech.twitterapi.controller;

import com.workintech.twitterapi.dto.CommentCreateRequest;
import com.workintech.twitterapi.dto.CommentResponse;
import com.workintech.twitterapi.dto.CommentUpdateRequest;
import com.workintech.twitterapi.dto.MessageResponse;
import com.workintech.twitterapi.service.CommentService;
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
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(@Valid @RequestBody CommentCreateRequest request) {
        return commentService.create(request);
    }

    @PutMapping("/{id}")
    public CommentResponse update(@PathVariable Long id, @Valid @RequestBody CommentUpdateRequest request) {
        return commentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable Long id) {
        commentService.delete(id);
        return new MessageResponse("Comment deleted");
    }

    @GetMapping("/findByTweetId")
    public List<CommentResponse> findByTweetId(@RequestParam Long tweetId) {
        return commentService.findByTweetId(tweetId);
    }
}
