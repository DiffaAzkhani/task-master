package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "Username or Email is required")
    @Size(max = 100, message = "Username or Email should not be more than 100 characters")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password should be between 8 and 20 characters")
    private String password;

}
