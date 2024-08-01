package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.DeleteUserRequest;
import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.request.UpdateUserRequest;
import com.taskmaster.taskmaster.model.response.RegisterResponse;
import com.taskmaster.taskmaster.model.response.UpdateUserResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping(
        path = "/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<RegisterResponse>> registerUser(
        @Valid @RequestBody RegisterRequest request
    ) {
        RegisterResponse response = userService.register(request);

        return ResponseEntity.status(HttpStatus.OK)
            .body(WebResponse.<RegisterResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(response)
                .build());
    }

    @DeleteMapping(
        path = "/delete-user",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteUser(
        @Valid @RequestBody DeleteUserRequest deleteUserRequest
    ) {
        userService.deleteUserAccount(deleteUserRequest);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @PatchMapping(
        path = "/update-user/{username}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UpdateUserResponse> updateUser(
        @PathVariable("username") String username,
        @Valid @RequestBody UpdateUserRequest request
    ) {
        UpdateUserResponse userResponse = userService.updateUser(username, request);

        return WebResponse.<UpdateUserResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(userResponse)
            .build();
    }

}
