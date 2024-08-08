package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.DeleteUserRequest;
import com.taskmaster.taskmaster.model.request.RegisterRequest;
import com.taskmaster.taskmaster.model.request.UpdateUserProfileRequest;
import com.taskmaster.taskmaster.model.response.GetAllEnrolledUSerStudyResponse;
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
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping(
        path = "/register",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<RegisterResponse>> registerUser(
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
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteUser(
        @Valid @RequestBody DeleteUserRequest deleteUserRequest
    ) {
        userService.deleteUserAccount(deleteUserRequest);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @PatchMapping(
        path = "/profile/{username}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UpdateUserProfileResponse> updateUserProfile(
        @PathVariable String username,
        @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        UpdateUserProfileResponse userResponse = userService.updateUserProfile(username, request);

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
        @RequestParam String username,
        @RequestParam(name = "page") int page,
        @RequestParam(name = "size") int size
    ) {
        Page<GetAllEnrolledUSerStudyResponse> responsePage = userService.getEnrolledUserStudy(username, page, size);
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
        path = "/profile",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<GetUserProfileResponse> getUserProfile(
        @RequestParam String username
    ) {
        GetUserProfileResponse userProfileResponse = userService.getUserProfile(username);

        return WebResponse.<GetUserProfileResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(userProfileResponse)
            .build();
    }

}
