package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.CommentCreateRequest;
import com.workintech.twitterapi.dto.CommentResponse;
import com.workintech.twitterapi.dto.CommentUpdateRequest;
import com.workintech.twitterapi.entity.Comment;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.ApiException;
import com.workintech.twitterapi.repository.CommentRepository;
import com.workintech.twitterapi.security.CurrentUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TweetService tweetService;
    private final CurrentUserService currentUserService;

    @Transactional
    public CommentResponse create(CommentCreateRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        Tweet tweet = tweetService.findTweet(request.tweetId());
        Comment comment = Comment.builder()
                .content(request.content())
                .tweet(tweet)
                .user(currentUser)
                .build();
        return toResponse(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponse update(Long id, CommentUpdateRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        Comment comment = findComment(id);
        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new ApiException("Only the comment owner can update this comment", HttpStatus.FORBIDDEN);
        }
        comment.setContent(request.content());
        return toResponse(commentRepository.save(comment));
    }

    @Transactional
    public void delete(Long id) {
        User currentUser = currentUserService.getCurrentUser();
        Comment comment = findComment(id);
        boolean isCommentOwner = comment.getUser().getId().equals(currentUser.getId());
        boolean isTweetOwner = comment.getTweet().getUser().getId().equals(currentUser.getId());

        if (!isCommentOwner && !isTweetOwner) {
            throw new ApiException("Only the comment owner or tweet owner can delete this comment", HttpStatus.FORBIDDEN);
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findByTweetId(Long tweetId) {
        return commentRepository.findByTweetIdOrderByCreatedAtAsc(tweetId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ApiException("Comment not found", HttpStatus.NOT_FOUND));
    }

    private CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getTweet().getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
