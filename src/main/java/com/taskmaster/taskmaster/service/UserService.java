package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.request.UpdateUserProfileRequest;
import com.taskmaster.taskmaster.model.response.GetAllEnrolledUSerStudyResponse;
import com.taskmaster.taskmaster.model.response.GetAllUsersResponse;
import com.taskmaster.taskmaster.model.response.GetUserForAdminResponse;
import com.taskmaster.taskmaster.model.response.GetUserProfileResponse;
import com.taskmaster.taskmaster.model.response.RegisterResponse;
import com.taskmaster.taskmaster.model.response.UpdateUserProfileResponse;
import org.springframework.data.domain.Page;

public interface UserService {

    RegisterResponse register(RegisterRequest request);

    void deleteUserAccountForAdmin(String username);

    void deleteUserAccountForUser(String password);

    UpdateUserProfileResponse updateUserProfileForAdmin(Long userId, UpdateUserProfileRequest request);

    UpdateUserProfileResponse updateUserProfileForUser(UpdateUserProfileRequest request);

    Page<GetAllUsersResponse> getAllUsers(int page, int size);

    GetUserForAdminResponse getUserForAdmin(Long userId);

    Page<GetAllEnrolledUSerStudyResponse> getEnrolledUserStudy(int page, int size);

    GetUserProfileResponse getUserProfile();

}
