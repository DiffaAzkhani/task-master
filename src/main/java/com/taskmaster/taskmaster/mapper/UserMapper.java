package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Role;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.enums.UserRole;
import com.taskmaster.taskmaster.model.response.GetAllUsersResponse;
import com.taskmaster.taskmaster.model.response.GetUserForAdminResponse;
import com.taskmaster.taskmaster.model.response.GetUserProfileResponse;
import com.taskmaster.taskmaster.model.response.LoginResponse;
import com.taskmaster.taskmaster.model.response.RegisterResponse;
import com.taskmaster.taskmaster.model.response.UpdateUserProfileResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    public UpdateUserProfileResponse toUpdateUserResponse(User user) {
        return UpdateUserProfileResponse.builder()
            .username(user.getUsername())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .profileImage(user.getProfileImage())
            .build();
    }

    public GetAllUsersResponse toGetAllUsersResponse(User user){
        List<UserRole> roles = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList());

        return GetAllUsersResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .userRole(roles)
            .createdAt(TimeUtil.formatToString(user.getCreatedAt()))
            .updatedAt(TimeUtil.formatToString(user.getUpdatedAt()))
            .build();
    }

    public GetUserForAdminResponse toGetUserForAdminResponse(User user) {
        return GetUserForAdminResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .createdAt(TimeUtil.formatToString(user.getCreatedAt()))
            .updatedAt(TimeUtil.formatToString(user.getUpdatedAt()))
            .build();
    }

    public GetUserProfileResponse toGetUserProfile(User user) {
        return GetUserProfileResponse.builder()
            .username(user.getUsername())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .profileImage(user.getProfileImage())
            .build();
    }

}
