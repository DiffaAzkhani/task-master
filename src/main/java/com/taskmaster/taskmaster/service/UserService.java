package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.DeleteUserRequest;
import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.request.UpdateUserRequest;
import com.taskmaster.taskmaster.model.response.RegisterResponse;
import com.taskmaster.taskmaster.model.response.UpdateUserResponse;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    void deleteUserAccount(DeleteUserRequest request);

    UpdateUserResponse updateUser(String username, UpdateUserRequest request);

}
