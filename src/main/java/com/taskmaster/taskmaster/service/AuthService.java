package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.LoginRequest;
import com.taskmaster.taskmaster.model.response.UserResponse;

public interface AuthService {

    UserResponse login (LoginRequest loginRequest);

    String createToken(String usernameOrEmail);

}
