package com.workintech.twitterapi.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.workintech.twitterapi.dto.RetweetRequest;
import com.workintech.twitterapi.entity.Retweet;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.ApiException;
import com.workintech.twitterapi.repository.RetweetRepository;
import com.workintech.twitterapi.security.CurrentUserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RetweetServiceTest {

    @Mock
    private RetweetRepository retweetRepository;

    @Mock
    private TweetService tweetService;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private RetweetService retweetService;

    @Test
    void retweetRejectsDuplicateRetweet() {
        User currentUser = User.builder().id(1L).username("ada").build();
        Tweet tweet = Tweet.builder().id(10L).build();

        when(currentUserService.getCurrentUser()).thenReturn(currentUser);
        when(tweetService.findTweet(10L)).thenReturn(tweet);
        when(retweetRepository.existsByTweetIdAndUserId(10L, 1L)).thenReturn(true);

        assertThatThrownBy(() -> retweetService.retweet(new RetweetRequest(10L)))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void deleteRejectsNonOwner() {
        User owner = User.builder().id(1L).username("owner").build();
        User otherUser = User.builder().id(2L).username("other").build();
        Retweet retweet = Retweet.builder().id(99L).user(owner).build();

        when(currentUserService.getCurrentUser()).thenReturn(otherUser);
        when(retweetRepository.findById(99L)).thenReturn(Optional.of(retweet));

        assertThatThrownBy(() -> retweetService.delete(99L))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void deleteRemovesRetweetWhenCurrentUserIsOwner() {
        User owner = User.builder().id(1L).username("owner").build();
        Retweet retweet = Retweet.builder().id(99L).user(owner).build();

        when(currentUserService.getCurrentUser()).thenReturn(owner);
        when(retweetRepository.findById(99L)).thenReturn(Optional.of(retweet));

        retweetService.delete(99L);

        verify(retweetRepository).delete(retweet);
    }
}
