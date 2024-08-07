package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyFilter;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.model.response.GetAllStudiesResponse;
import com.taskmaster.taskmaster.model.response.GetStudyByCodeResponse;
import com.taskmaster.taskmaster.model.response.PagingResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.StudyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/study")
public class StudyController {

    private final StudyService studyService;

    @GetMapping(
        path = "/{studyCode}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<GetStudyByCodeResponse> getStudy(
        @PathVariable("studyCode") String studyCode
    ){
        GetStudyByCodeResponse studyResponse = studyService.getStudyByCode(studyCode);

        return WebResponse.<GetStudyByCodeResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(studyResponse)
                .build();
    }

    @GetMapping(
        path = "/all-study",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<GetAllStudiesResponse>> getAllStudy(
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

}
