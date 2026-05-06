package com.workintech.twitterapi.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.ApiException;
import com.workintech.twitterapi.repository.CommentRepository;
import com.workintech.twitterapi.repository.RetweetRepository;
import com.workintech.twitterapi.repository.TweetLikeRepository;
import com.workintech.twitterapi.repository.TweetRepository;
import com.workintech.twitterapi.security.CurrentUserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private TweetLikeRepository tweetLikeRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private RetweetRepository retweetRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private TweetService tweetService;

    @Test
    void deleteAllowsOnlyTweetOwner() {
        User owner = User.builder().id(1L).username("owner").build();
        User otherUser = User.builder().id(2L).username("other").build();
        Tweet tweet = Tweet.builder().id(10L).user(owner).content("hello").build();

        when(currentUserService.getCurrentUser()).thenReturn(otherUser);
        when(tweetRepository.findById(10L)).thenReturn(Optional.of(tweet));

        assertThatThrownBy(() -> tweetService.delete(10L))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void deleteRemovesTweetWhenCurrentUserIsOwner() {
        User owner = User.builder().id(1L).username("owner").build();
        Tweet tweet = Tweet.builder().id(10L).user(owner).content("hello").build();

        when(currentUserService.getCurrentUser()).thenReturn(owner);
        when(tweetRepository.findById(10L)).thenReturn(Optional.of(tweet));

        tweetService.delete(10L);

        verify(commentRepository).deleteByTweetId(10L);
        verify(tweetLikeRepository).deleteByTweetId(10L);
        verify(retweetRepository).deleteByTweetId(10L);
        verify(tweetRepository).delete(tweet);
    }
}
