package com.workintech.twitterapi.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.workintech.twitterapi.entity.Comment;
import com.workintech.twitterapi.entity.Tweet;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.ApiException;
import com.workintech.twitterapi.repository.CommentRepository;
import com.workintech.twitterapi.security.CurrentUserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TweetService tweetService;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private CommentService commentService;

    @Test
    void deleteAllowsTweetOwnerToDeleteComment() {
        User tweetOwner = User.builder().id(1L).username("tweetOwner").build();
        User commentOwner = User.builder().id(2L).username("commentOwner").build();
        Tweet tweet = Tweet.builder().id(10L).user(tweetOwner).build();
        Comment comment = Comment.builder().id(20L).tweet(tweet).user(commentOwner).build();

        when(currentUserService.getCurrentUser()).thenReturn(tweetOwner);
        when(commentRepository.findById(20L)).thenReturn(Optional.of(comment));

        commentService.delete(20L);

        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteRejectsUserWhoOwnsNeitherTweetNorComment() {
        User tweetOwner = User.builder().id(1L).username("tweetOwner").build();
        User commentOwner = User.builder().id(2L).username("commentOwner").build();
        User otherUser = User.builder().id(3L).username("other").build();
        Tweet tweet = Tweet.builder().id(10L).user(tweetOwner).build();
        Comment comment = Comment.builder().id(20L).tweet(tweet).user(commentOwner).build();

        when(currentUserService.getCurrentUser()).thenReturn(otherUser);
        when(commentRepository.findById(20L)).thenReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.delete(20L))
                .isInstanceOf(ApiException.class);
    }
}
