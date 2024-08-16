package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.model.response.RefreshJwtTokenResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public RefreshJwtTokenResponse toRefreshJwtTokenResponse(User user) {
        return RefreshJwtTokenResponse.builder()
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }

}
