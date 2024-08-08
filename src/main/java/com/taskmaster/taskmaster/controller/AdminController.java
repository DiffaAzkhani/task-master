package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyFilter;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.model.request.CreateNewStudyRequest;
import com.taskmaster.taskmaster.model.request.DeleteUserRequest;
import com.taskmaster.taskmaster.model.request.UpdateStudyRequest;
import com.taskmaster.taskmaster.model.request.UpdateUserProfileRequest;
import com.taskmaster.taskmaster.model.response.CreateNewStudyResponse;
import com.taskmaster.taskmaster.model.response.GetAllStudiesResponse;
import com.taskmaster.taskmaster.model.response.GetAllUsersResponse;
import com.taskmaster.taskmaster.model.response.GetStudyByCodeResponse;
import com.taskmaster.taskmaster.model.response.GetUserForAdminResponse;
import com.taskmaster.taskmaster.model.response.PagingResponse;
import com.taskmaster.taskmaster.model.response.UpdateStudyResponse;
import com.taskmaster.taskmaster.model.response.UpdateUserProfileResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.StudyService;
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
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final UserService userService;

    private final StudyService studyService;

    // ADMIN API PANEL FOR CONTROLLING USER

    @DeleteMapping(
        path = "/users",
        consumes = MediaType.APPLICATION_JSON_VALUE
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
        path = "/users/{username}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UpdateUserProfileResponse> updateUser(
        @PathVariable("username") String username,
        @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        UpdateUserProfileResponse userResponse = userService.updateUser(username, request);

        return WebResponse.<UpdateUserProfileResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(userResponse)
            .build();
    }

    @GetMapping(
        path = "/all-users",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<GetAllUsersResponse>> getAllUsers(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetAllUsersResponse> allUsersResponsePage = userService.getAllUsers(page, size);
        List<GetAllUsersResponse> usersResponses = allUsersResponsePage.getContent();

        return WebResponse.<List<GetAllUsersResponse>>builder()
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
        path = "/users/{username}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<GetUserForAdminResponse> getUser(
        @PathVariable(name = "username") String username
    ) {
        GetUserForAdminResponse getUserForAdminResponse = userService.getUser(username);

        return WebResponse.<GetUserForAdminResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(getUserForAdminResponse)
            .build();
    }

    // ADMIN API PANEL FOR CONTROLLING STUDY

    @GetMapping(
        path = "/studies",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<GetAllStudiesResponse>> getAllStudies(
        @RequestParam(name = "type", required = false) StudyType studyType,
        @RequestParam(name = "categories", required = false) Set<StudyCategory> categories,
        @RequestParam(name = "levels", required = false) Set<StudyLevel> levels,
        @RequestParam(name = "study-filter", required = false) StudyFilter studyFilters,
        @RequestParam(name = "min-price", required = false) Double minPrice,
        @RequestParam(name = "max-price", required = false) Double maxPrice,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetAllStudiesResponse> studyResponsePage = studyService.getAllStudies(studyType, categories, levels, studyFilters, minPrice, maxPrice, page, size);
        List<GetAllStudiesResponse> studyResponses = studyResponsePage.getContent();

        return WebResponse.<List<GetAllStudiesResponse>>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(studyResponses)
            .paging(PagingResponse.builder()
                .currentPage(page)
                .size(size)
                .totalPage(studyResponsePage.getTotalPages())
                .totalElement(studyResponsePage.getTotalElements())
                .empty(studyResponsePage.isEmpty())
                .first(studyResponsePage.isFirst())
                .last(studyResponsePage.isLast())
                .build())
            .build();
    }

    @GetMapping(
        path = "/studies/{studyCode}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<GetStudyByCodeResponse> getStudyByCode(
        @PathVariable("studyCode") String studyCode
    ){
        GetStudyByCodeResponse studyResponse = studyService.getStudyByCode(studyCode);

        return WebResponse.<GetStudyByCodeResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(studyResponse)
            .build();
    }

    @PostMapping(
        path = "/studies",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<CreateNewStudyResponse>> addStudy(
        @Valid @RequestBody CreateNewStudyRequest request
    ) {
        CreateNewStudyResponse newStudyResponse = studyService.createNewStudy(request);

        return ResponseEntity.status(HttpStatus.OK)
            .body(WebResponse.<CreateNewStudyResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(newStudyResponse)
                .build());
    }

    @DeleteMapping(
        path = "/studies/{studyCode}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteStudy(
        @PathVariable("studyCode") String studyCode
    ) {
        studyService.deleteStudy(studyCode);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @PatchMapping(
        path = "/studies/{studyCode}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UpdateStudyResponse> updateStudy(
        @PathVariable("studyCode") String studyCode,
        @Valid @RequestBody UpdateStudyRequest request
    ) {
        UpdateStudyResponse updateStudyResponse = studyService.updateStudy(studyCode, request);

        return WebResponse.<UpdateStudyResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(updateStudyResponse)
            .build();
    }

}
