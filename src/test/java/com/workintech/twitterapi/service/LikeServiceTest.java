package com.workintech.twitterapi.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.workintech.twitterapi.dto.LikeRequest;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.TweetLike;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.ApiException;
import com.workintech.twitterapi.repository.TweetLikeRepository;
import com.workintech.twitterapi.security.CurrentUserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private TweetLikeRepository tweetLikeRepository;

    @Mock
    private TweetService tweetService;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private LikeService likeService;

    @Test
    void likeRejectsDuplicateLike() {
        User currentUser = User.builder().id(1L).username("ada").build();
        Tweet tweet = Tweet.builder().id(10L).build();

        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(tweetService.findTweet(10L)).thenReturn(tweet);
        when(tweetLikeRepository.existsByTweetIdAndUserId(10L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> likeService.like(new LikeRequest(10L)))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void dislikeDeletesExistingLike() {
        User currentUser = User.builder().id(1L).username("ada").build();
        TweetLike like = TweetLike.builder().id(50L).build();

        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(tweetLikeRepository.findByTweetIdAndUserId(10L, 1L)).thenReturn(Optional.of(like));

        likeService.dislike(new LikeRequest(10L));

        verify(tweetLikeRepository).delete(like);
    }
}
