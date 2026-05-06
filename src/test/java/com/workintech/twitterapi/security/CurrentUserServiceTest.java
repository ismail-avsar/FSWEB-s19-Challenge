package com.workintech.twitterapi.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.ApiException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

class CurrentUserServiceTest {

    private final CurrentUserService currentUserService = new CurrentUserService();

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUserReturnsUserFromSecurityContext() {
        User user = User.builder().id(1L).username("ada").build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, List.of())
        );

        User currentUser = currentUserService.getCurrentUser();

        assertThat(currentUser).isSameAs(user);
    }

    @Test
    void getCurrentUserRejectsAnonymousRequests() {
        assertThatThrownBy(currentUserService::getCurrentUser)
                .isInstanceOf(ApiException.class);
    }
}
