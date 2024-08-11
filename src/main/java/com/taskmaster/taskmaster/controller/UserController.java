package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.request.UpdateUserProfileRequest;
import com.taskmaster.taskmaster.model.response.GetAllEnrolledUSerStudyResponse;
import com.taskmaster.taskmaster.model.response.GetAllUsersResponse;
import com.taskmaster.taskmaster.model.response.GetUserForAdminResponse;
import com.taskmaster.taskmaster.model.response.GetUserProfileResponse;
import com.taskmaster.taskmaster.model.response.PagingResponse;
import com.taskmaster.taskmaster.model.response.PagingWebResponse;
import com.taskmaster.taskmaster.model.response.RegisterResponse;
import com.taskmaster.taskmaster.model.response.UpdateUserProfileResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PagingWebResponse<List<GetAllUsersResponse>> getAllUsers(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetAllUsersResponse> allUsersResponsePage = userService.getAllUsers(page, size);
        List<GetAllUsersResponse> usersResponses = allUsersResponsePage.getContent();

        return PagingWebResponse.<List<GetAllUsersResponse>>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(usersResponses)
            .paging(PagingResponse.builder()
                .currentPage(page)
                .size(size)
                .totalPage(allUsersResponsePage.getTotalPages())
                .totalElement(allUsersResponsePage.getTotalElements())
                .empty(allUsersResponsePage.isEmpty())
                .first(allUsersResponsePage.isFirst())
                .last(allUsersResponsePage.isLast())
                .build())
            .build();
    }

    @GetMapping(
        path = "/{userId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<GetUserForAdminResponse> getUserByIdForAdmin(
        @PathVariable(name = "userId") Long userId
    ) {
        GetUserForAdminResponse getUserForAdminResponse = userService.getUserForAdmin(userId);

        return WebResponse.<GetUserForAdminResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(getUserForAdminResponse)
            .build();
    }

    @PatchMapping(
        path = "/{userId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UpdateUserProfileResponse> updateUserByIdForAdmin(
        @PathVariable(name = "userId") Long userId,
        @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        UpdateUserProfileResponse userResponse = userService.updateUserProfileForAdmin(userId, request);

        return WebResponse.<UpdateUserProfileResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(userResponse)
            .build();
    }

    @DeleteMapping
    public WebResponse<String> deleteUserByIdForAdmin(
        @RequestParam(name = "userId") Long userId
    ) {
        userService.deleteUserAccountForAdmin(userId);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    // API Path for USER Role

    @PostMapping(
        path = "/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<RegisterResponse>> register(
        @Valid @RequestBody RegisterRequest request
    ) {
        RegisterResponse response = userService.register(request);

        return ResponseEntity.status(HttpStatus.OK)
            .body(WebResponse.<RegisterResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(response)
                .build());
    }

    @DeleteMapping(
        path = "/me"
    )
    public WebResponse<String> deleteUser(
        @RequestParam(name = "password") String password
    ) {
        userService.deleteUserAccountForUser(password);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @PatchMapping(
        path = "/me/profile",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UpdateUserProfileResponse> updateUserProfile(
        @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        UpdateUserProfileResponse userResponse = userService.updateUserProfileForUser(request);

        return WebResponse.<UpdateUserProfileResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(userResponse)
            .build();
    }

    @GetMapping(
        path = "/my-studies",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PagingWebResponse<List<GetAllEnrolledUSerStudyResponse>> getAllEnrolledUserStudy(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetAllEnrolledUSerStudyResponse> responsePage = userService.getEnrolledUserStudy(page, size);
        List<GetAllEnrolledUSerStudyResponse> allEnrolledUSerStudyResponses = responsePage.getContent();

        return PagingWebResponse.<List<GetAllEnrolledUSerStudyResponse>>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(allEnrolledUSerStudyResponses)
            .paging(PagingResponse.builder()
                .currentPage(page)
                .size(size)
                .totalPage(responsePage.getTotalPages())
                .totalElement(responsePage.getTotalElements())
                .empty(responsePage.isEmpty())
                .first(responsePage.isFirst())
                .last(responsePage.isLast())
                .build())
            .build();
    }

    @GetMapping(
        path = "/me/profile",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<GetUserProfileResponse> getUserProfile(
    ) {
        GetUserProfileResponse userProfileResponse = userService.getUserProfile();

        return WebResponse.<GetUserProfileResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(userProfileResponse)
            .build();
    }

}
