package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.LoginRequest;
import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.response.RegisterResponse;
import com.taskmaster.taskmaster.model.response.UserResponse;

public interface AuthService {
    UserResponse login (LoginRequest loginRequest);

    RegisterResponse register(RegisterRequest request);
}
