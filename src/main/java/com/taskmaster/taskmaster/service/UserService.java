package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.DeleteUserRequest;
import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.response.RegisterResponse;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    void deleteUserAccount(DeleteUserRequest request);

}
