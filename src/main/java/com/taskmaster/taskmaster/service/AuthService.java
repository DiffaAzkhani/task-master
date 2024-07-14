package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.LoginRequest;
import com.taskmaster.taskmaster.model.response.LoginResponse;

public interface AuthService {

    LoginResponse login (LoginRequest loginRequest);

    String createToken(String username, String email);

}
