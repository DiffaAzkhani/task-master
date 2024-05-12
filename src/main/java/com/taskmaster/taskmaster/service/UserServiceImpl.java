package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.model.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .createdAt(TimeUtil.formatToString(user.getCreatedAt()))
            .updatedAt(TimeUtil.formatToString(user.getUpdatedAt()))
            .build();
    }
}
