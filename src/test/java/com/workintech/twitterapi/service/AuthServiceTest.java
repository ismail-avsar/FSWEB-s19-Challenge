package com.workintech.twitterapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.workintech.twitterapi.dto.AuthResponse;
import com.workintech.twitterapi.dto.LoginRequest;
import com.workintech.twitterapi.dto.RegisterRequest;
import com.workintech.twitterapi.entity.User;
import com.workintech.twitterapi.exception.ApiException;
import com.workintech.twitterapi.repository.UserRepository;
import com.workintech.twitterapi.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerCreatesUserWithEncodedPasswordAndReturnsToken() {
        RegisterRequest request = new RegisterRequest("ada", "ada@example.com", "secret123");
        User savedUser = User.builder()
                .id(1L)
                .username("ada")
                .email("ada@example.com")
                .password("encoded")
                .build();

        when(passwordEncoder.encode("secret123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(savedUser)).thenReturn("token");

        AuthResponse response = authService.register(request);

        assertThat(response.token()).isEqualTo("token");
        assertThat(response.userId()).isEqualTo(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerRejectsDuplicateUsername() {
        RegisterRequest request = new RegisterRequest("ada", "ada@example.com", "secret123");
        when(userRepository.existsByUsername("ada")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void loginReturnsTokenForCorrectPassword() {
        LoginRequest request = new LoginRequest("ada", "secret123");
        User user = User.builder().id(1L).username("ada").password("encoded").build();

        when(userRepository.findByUsernameOrEmail("ada", "ada")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret123", "encoded")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("token");

        AuthResponse response = authService.login(request);

        assertThat(response.token()).isEqualTo("token");
    }

    @Test
    void loginRejectsWrongPassword() {
        LoginRequest request = new LoginRequest("ada", "wrong");
        User user = User.builder().id(1L).username("ada").password("encoded").build();

        when(userRepository.findByUsernameOrEmail("ada", "ada")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(ApiException.class);
    }
}
