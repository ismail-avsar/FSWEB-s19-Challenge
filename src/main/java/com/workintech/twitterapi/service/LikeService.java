package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.LikeRequest;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.TweetLike;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.ApiException;
import com.workintech.twitterapi.repository.TweetLikeRepository;
import com.workintech.twitterapi.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final TweetLikeRepository tweetLikeRepository;
    private final TweetService tweetService;
    private final CurrentUserService currentUserService;

    @Transactional
    public void like(LikeRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        Tweet tweet = tweetService.findTweet(request.tweetId());

        if (tweetLikeRepository.existsByTweetIdAndUserId(tweet.getId(), currentUser.getId())) {
            throw new ApiException("This tweet is already liked by the user", HttpStatus.CONFLICT);
        }

        TweetLike like = TweetLike.builder()
                .tweet(tweet)
                .user(currentUser)
                .build();
        tweetLikeRepository.save(like);
    }

    @Transactional
    public void dislike(LikeRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        TweetLike like = tweetLikeRepository
                .findByTweetIdAndUserId(request.tweetId(), currentUser.getId())
                .orElseThrow(() -> new ApiException("Like not found", HttpStatus.NOT_FOUND));
        tweetLikeRepository.delete(like);
    }
}
