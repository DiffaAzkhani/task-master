package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.model.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(TimeUtil.formatToString(user.getCreated_at()))
                .updatedAt(TimeUtil.formatToString(user.getCreated_at()))
                .build();
    }
}
