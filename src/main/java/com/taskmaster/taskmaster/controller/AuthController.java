package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.LoginRequest;
import com.taskmaster.taskmaster.model.response.LoginResponse;
import com.taskmaster.taskmaster.model.response.RefreshJwtTokenResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<LoginResponse>> login(
        @Valid @RequestBody LoginRequest request,
        HttpServletResponse httpServletResponse
    ) {
        LoginResponse response = authService.login(request);
        String accessToken = authService.createToken(response.getUsername(), response.getEmail());
        String refreshToken = authService.createRefreshToken(response.getUsername());

        authService.createRefreshTokenCookie(httpServletResponse, refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .body(WebResponse.<LoginResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(response)
                .build());
    }

    @PostMapping(
        path = "/refresh",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<RefreshJwtTokenResponse>> refreshToken(
        @CookieValue("refresh_token") String refreshToken
    ) {
        RefreshJwtTokenResponse response = authService.refreshToken(refreshToken);
        String accessToken = authService.createToken(response.getUsername(), response.getEmail());

        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .body(WebResponse.<RefreshJwtTokenResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(response)
                .build());
    }

    @PostMapping(
        path = "/logout"
    )
    public WebResponse<String> logout(
        @CookieValue("refresh_token") String refreshToken,
        HttpServletResponse httpServletResponse
    ) {
        authService.logout(refreshToken, httpServletResponse);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @PostMapping(
        path = "/revoke"
    )
    public WebResponse<String> revokeUserInAllDevice(
        @CookieValue("refresh_token") String refreshToken
    ) {
        authService.revokeUserInAllDevice(refreshToken);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @PostMapping(
        path = "/revoke/{userId}"
    )
    public WebResponse<String> revokeUserInAllDeviceForAdmin(
        @PathVariable(name = "userId") Long userId
    ) {
        authService.revokeUserInAllDeviceForAdmin(userId);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

}
