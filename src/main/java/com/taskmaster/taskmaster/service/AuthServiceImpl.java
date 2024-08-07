package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.mapper.UserMapper;
import com.taskmaster.taskmaster.model.request.LoginRequest;
import com.taskmaster.taskmaster.model.response.LoginResponse;
import com.taskmaster.taskmaster.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsernameOrEmail(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found!"));

        if (!user.isEnabled()) {
            log.warn("login attempt from disabled user: {}", loginRequest.getUsernameOrEmail());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Please verify your email first!");
        }

        log.info("Successfull login for user: {}", loginRequest.getUsernameOrEmail());

        return userMapper.toLoginResponse(user);
    }

    @Override
    @Transactional
    public String createToken(String username, String email) {
        User user = userRepository.findByUsernameOrEmail(username, email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found!"));

        return jwtService.generateToken(user);
    }

}
