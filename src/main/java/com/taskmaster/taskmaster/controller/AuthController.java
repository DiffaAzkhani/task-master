package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.LoginRequest;
import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.response.RegisterResponse;
import com.taskmaster.taskmaster.model.response.UserResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<WebResponse<UserResponse>> login(@RequestBody LoginRequest request) {
        UserResponse userResponse = authService.login(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(WebResponse.<UserResponse>builder()
                        .code(HttpStatus.OK.value())
                        .message(HttpStatus.OK.getReasonPhrase())
                        .data(userResponse)
                        .build());
    }

    @PostMapping(
        path = "/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<RegisterResponse>> registerUser(@RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.OK)
            .body(WebResponse.<RegisterResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(response)
                .build());
    }
}