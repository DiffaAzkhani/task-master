package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.response.RefreshJwtTokenResponse;
import com.taskmaster.taskmaster.model.request.LoginRequest;
import com.taskmaster.taskmaster.model.response.LoginResponse;

import javax.servlet.http.HttpServletResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    void logout(String refreshToken, HttpServletResponse httpServletResponse);

    String createToken(String username, String email);

    RefreshJwtTokenResponse refreshToken(String refreshToken);

    void createRefreshTokenCookie(HttpServletResponse httpServletResponse, String refreshToken);

    String createRefreshToken(String username);

    void revokeUserInAllDevice(String refreshToken);

    void revokeUserInAllDeviceForAdmin(Long userId);

}
