package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.model.response.LoginResponse;
import com.taskmaster.taskmaster.model.response.RegisterResponse;
import com.taskmaster.taskmaster.model.response.UpdateUserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public LoginResponse toLoginResponse(User user) {
        return LoginResponse.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }

    public RegisterResponse toRegisterResponse(User user) {
        return RegisterResponse.builder()
            .email(user.getEmail())
            .username(user.getUsername())
            .build();
    }

    public UpdateUserResponse toUpdateUserResponse(User user) {
        return UpdateUserResponse.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }

}
