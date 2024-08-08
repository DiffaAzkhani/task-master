package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.DeleteUserRequest;
import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.request.UpdateUserProfileRequest;
import com.taskmaster.taskmaster.model.response.*;
import org.springframework.data.domain.Page;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    void deleteUserAccount(DeleteUserRequest request);

    UpdateUserProfileResponse updateUser(String username, UpdateUserProfileRequest request);

    Page<GetAllUsersResponse> getAllUsers(int page, int size);

    GetUserForAdminResponse getUser(String username);

    Page<GetAllEnrolledUSerStudyResponse> getEnrolledUserStudy(String username, int page, int size);

    GetUserProfileResponse getUserProfile(String username);

}
