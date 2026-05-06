package com.workintech.twitterapi.service;

import com.workintech.twitterapi.dto.RetweetRequest;
import com.workintech.twitterapi.entity.Retweet;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.ApiException;
import com.workintech.twitterapi.repository.RetweetRepository;
import com.workintech.twitterapi.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RetweetService {

    private final RetweetRepository retweetRepository;
    private final TweetService tweetService;
    private final CurrentUserService currentUserService;

    @Transactional
    public void retweet(RetweetRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        Tweet tweet = tweetService.findTweet(request.tweetId());

        if (retweetRepository.existsByTweetIdAndUserId(tweet.getId(), currentUser.getId())) {
            throw new ApiException("This tweet is already retweeted by the user", HttpStatus.CONFLICT);
        }

        Retweet retweet = Retweet.builder()
                .tweet(tweet)
                .user(currentUser)
                .build();
        retweetRepository.save(retweet);
    }

    @Transactional
    public void delete(Long id) {
        User currentUser = currentUserService.getCurrentUser();
        Retweet retweet = retweetRepository.findById(id)
                .orElseThrow(() -> new ApiException("Retweet not found", HttpStatus.NOT_FOUND));

        if (!retweet.getUser().getId().equals(currentUser.getId())) {
            throw new ApiException("Only the retweet owner can delete this retweet", HttpStatus.FORBIDDEN);
        }

        retweetRepository.delete(retweet);
    }
}
