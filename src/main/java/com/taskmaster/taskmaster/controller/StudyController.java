package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyFilter;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.model.request.CreateNewStudyRequest;
import com.taskmaster.taskmaster.model.request.UpdateStudyRequest;
import com.taskmaster.taskmaster.model.response.CreateNewStudyResponse;
import com.taskmaster.taskmaster.model.response.GetAllStudiesResponse;
import com.taskmaster.taskmaster.model.response.GetStudyByCodeResponse;
import com.taskmaster.taskmaster.model.response.PagingResponse;
import com.taskmaster.taskmaster.model.response.PagingWebResponse;
import com.taskmaster.taskmaster.model.response.UpdateStudyResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.StudyService;
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
@RequestMapping("/api/v1/studies")
public class StudyController {

    private final StudyService studyService;

    // API Path for Admin Role

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PagingWebResponse<List<GetAllStudiesResponse>> getAllStudies(
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

        return PagingWebResponse.<List<GetAllStudiesResponse>>builder()
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

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<CreateNewStudyResponse>> createNewStudy(
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

    @GetMapping(
        path = "/{studyId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<GetStudyByCodeResponse> getStudy(
        @PathVariable(name = "studyId") Long studyId
    ){
        GetStudyByCodeResponse studyResponse = studyService.getStudyById(studyId);

        return WebResponse.<GetStudyByCodeResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(studyResponse)
            .build();
    }

    @PatchMapping(
        path = "/{studyId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UpdateStudyResponse> updateStudyById(
        @PathVariable("studyId") Long studyId,
        @Valid @RequestBody UpdateStudyRequest request
    ) {
        UpdateStudyResponse updateStudyResponse = studyService.updateStudy(studyId, request);

        return WebResponse.<UpdateStudyResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(updateStudyResponse)
            .build();
    }

    @DeleteMapping(
        path = "/{studyId}"
    )
    public WebResponse<String> deleteStudyById(
        @PathVariable(name = "studyId") Long studyId
    ) {
        studyService.deleteStudy(studyId);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    // API Path for User Role

}
