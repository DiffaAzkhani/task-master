package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.model.request.LoginRequest;
import com.taskmaster.taskmaster.model.response.UserResponse;
import com.taskmaster.taskmaster.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    LoginRequest request;

    @BeforeEach
    public void setUp() {
        request = LoginRequest.builder()
            .username("username")
            .password("password")
            .build();
    }

    @Test
    void testLogin_userFoundAndEnabled() {
        User user = User.builder()
            .username("username")
            .enabled(true)
            .build();

        when(userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(Optional.of(user));

        UserResponse userResponse = authService.login(request);

        Assertions.assertEquals(user.getUsername(),userResponse.getUsername());
    }

    @Test
    void testLogin_userNotEnabled() {
        User user = User.builder()
            .username("username")
            .enabled(false)
            .build();

        when(userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(Optional.of(user));

        Assertions.assertThrows(ResponseStatusException.class, () -> authService.login(request));
    }

    @Test
    void testLogin_userNotFound() {
        when(userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> authService.login(request));
    }

    @Test
    void testLogin_InvalidPassword() {
        User user = User.builder()
            .username("username")
            .password("validPassword")
            .enabled(true)
            .build();

        when(userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword())).thenReturn(Optional.of(user));

        request.setPassword("invalid_password");

        Assertions.assertThrows(ResponseStatusException.class, () -> authService.login(request));
    }

}
