package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.TweetCreateRequest;
import com.workintech.twitterapi.dto.TweetResponse;
import com.workintech.twitterapi.dto.TweetUpdateRequest;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.ApiException;
import com.workintech.twitterapi.repository.CommentRepository;
import com.workintech.twitterapi.repository.RetweetRepository;
import com.workintech.twitterapi.repository.TweetLikeRepository;
import com.workintech.twitterapi.repository.TweetRepository;
import com.workintech.twitterapi.security.CurrentUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TweetService {

    private final TweetRepository tweetRepository;
    private final TweetLikeRepository tweetLikeRepository;
    private final CommentRepository commentRepository;
    private final RetweetRepository retweetRepository;
    private final CurrentUserService currentUserService;

    @Transactional
    public TweetResponse create(TweetCreateRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        Tweet tweet = Tweet.builder()
                .content(request.content())
                .user(currentUser)
                .build();
        return toResponse(tweetRepository.save(tweet));
    }

    @Transactional(readOnly = true)
    public List<TweetResponse> findByUserId(Long userId) {
        return tweetRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TweetResponse findById(Long id) {
        return toResponse(findTweet(id));
    }

    @Transactional
    public TweetResponse update(Long id, TweetUpdateRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        Tweet tweet = findTweet(id);
        ensureOwner(tweet, currentUser);
        tweet.setContent(request.content());
        return toResponse(tweetRepository.save(tweet));
    }

    @Transactional
    public void delete(Long id) {
        User currentUser = currentUserService.getCurrentUser();
        Tweet tweet = findTweet(id);
        ensureOwner(tweet, currentUser);
        commentRepository.deleteByTweetId(tweet.getId());
        tweetLikeRepository.deleteByTweetId(tweet.getId());
        retweetRepository.deleteByTweetId(tweet.getId());
        tweetRepository.delete(tweet);
    }

    public Tweet findTweet(Long id) {
        return tweetRepository.findById(id)
                .orElseThrow(() -> new ApiException("Tweet not found", HttpStatus.NOT_FOUND));
    }

    private void ensureOwner(Tweet tweet, User currentUser) {
        if (!tweet.getUser().getId().equals(currentUser.getId())) {
            throw new ApiException("Only the tweet owner can do this operation", HttpStatus.FORBIDDEN);
        }
    }

    private TweetResponse toResponse(Tweet tweet) {
        Long tweetId = tweet.getId();
        return new TweetResponse(
                tweet.getId(),
                tweet.getContent(),
                tweet.getUser().getId(),
                tweet.getUser().getUsername(),
                tweetLikeRepository.countByTweetId(tweetId),
                commentRepository.countByTweetId(tweetId),
                retweetRepository.countByTweetId(tweetId),
                tweet.getCreatedAt(),
                tweet.getUpdatedAt()
        );
    }
}
