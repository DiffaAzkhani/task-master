package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyFilter;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.model.request.AddStudyRequest;
import com.taskmaster.taskmaster.model.request.UpdateStudyRequest;
import com.taskmaster.taskmaster.model.response.AddStudyResponse;
import com.taskmaster.taskmaster.model.response.GetAllStudyResponse;
import com.taskmaster.taskmaster.model.response.GetStudyResponse;
import com.taskmaster.taskmaster.model.response.PagingResponse;
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

    @PostMapping(
        path = "/add-study",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<AddStudyResponse>> addStudy(
        @Valid @RequestBody AddStudyRequest request
    ) {
        AddStudyResponse studyResponse = studyService.addStudy(request);

        return ResponseEntity.status(HttpStatus.OK)
            .body(WebResponse.<AddStudyResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(studyResponse)
                .build());
    }

    @GetMapping(
        path = "/{studyCode}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<GetStudyResponse> getStudy(
        @PathVariable("studyCode") String studyCode
    ){
        GetStudyResponse studyResponse = studyService.getStudy(studyCode);

        return WebResponse.<GetStudyResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(studyResponse)
                .build();
    }

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<GetAllStudyResponse>> getAllStudy(
        @RequestParam(name = "type", required = false) StudyType studyType,
        @RequestParam(name = "categories", required = false) Set<StudyCategory> categories,
        @RequestParam(name = "levels", required = false) Set<StudyLevel> levels,
        @RequestParam(name = "study-filter", required = false) StudyFilter studyFilters,
        @RequestParam(name = "min-price", required = false) Double minPrice,
        @RequestParam(name = "max-price", required = false) Double maxPrice,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetAllStudyResponse> studyResponsePage = studyService.getAllStudy(studyType, categories, levels, studyFilters, minPrice, maxPrice, page, size);
        List<GetAllStudyResponse> studyResponses = studyResponsePage.getContent();

        return WebResponse.<List<GetAllStudyResponse>>builder()
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

    @DeleteMapping(
        path = "/delete/{studyCode}",
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
        path = "/{studyCode}",
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
