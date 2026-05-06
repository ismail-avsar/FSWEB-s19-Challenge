package com.workintech.twitterapi.security;

import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new ApiException("Login is required for this operation", HttpStatus.UNAUTHORIZED);
        }
        return user;
    }
}
