package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.response.RegisterResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.UserService;
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
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping(
        path = "/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<RegisterResponse>> registerUser(@RequestBody RegisterRequest request) {
        RegisterResponse response = userService.register(request);

        return ResponseEntity.status(HttpStatus.OK)
            .body(WebResponse.<RegisterResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(response)
                .build());
    }

}
