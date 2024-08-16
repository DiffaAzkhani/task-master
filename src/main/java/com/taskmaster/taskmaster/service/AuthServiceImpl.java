package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.RefreshToken;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.mapper.AuthMapper;
import com.taskmaster.taskmaster.mapper.UserMapper;
import com.taskmaster.taskmaster.model.request.LoginRequest;
import com.taskmaster.taskmaster.model.response.LoginResponse;
import com.taskmaster.taskmaster.model.response.RefreshJwtTokenResponse;
import com.taskmaster.taskmaster.repository.RefreshTokenRepository;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    private final AuthMapper authMapper;

    @Override
    @Transactional
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

    @Override
    @Transactional
    public RefreshJwtTokenResponse refreshToken(String refreshToken) {
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired or Invalid refresh token!");
        }

        String username = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        return authMapper.toRefreshJwtTokenResponse(user);
    }

    @Override
    @Transactional
    public void createRefreshTokenCookie(HttpServletResponse httpServletResponse, String refreshToken) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/v1/auth/refresh");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        httpServletResponse.addCookie(cookie);
    }

    @Override
    @Transactional
    public String createRefreshToken(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

        RefreshToken refreshToken = RefreshToken.builder()
            .token(jwtService.generateRefreshToken(username))
            .username(user.getUsername())
            .issuedAt(TimeUtil.getFormattedLocalDateTimeNow())
            .expiredAt(TimeUtil.getFormattedLocalDateTimeNow().plusWeeks(1))
            .user(user)
            .build();

        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

}
