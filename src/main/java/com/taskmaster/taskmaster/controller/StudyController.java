package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.AddStudyRequest;
import com.taskmaster.taskmaster.model.request.UpdateStudyRequest;
import com.taskmaster.taskmaster.model.response.PagingResponse;
import com.taskmaster.taskmaster.model.response.StudyResponse;
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

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/learning-material")
public class StudyController {

    private final StudyService studyService;

    @PostMapping(
        path = "/add-study",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<StudyResponse>> addStudy(@RequestBody AddStudyRequest request) {
        StudyResponse studyResponse = studyService.addStudy(request);

        return ResponseEntity.status(HttpStatus.OK)
            .body(WebResponse.<StudyResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(studyResponse)
                .build());
    }

    @GetMapping(
        path = "/{studyCode}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<StudyResponse> getStudy(@PathVariable("studyCode") String studyCode){
        StudyResponse studyResponse = studyService.getStudy(studyCode);

        return WebResponse.<StudyResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(studyResponse)
                .build();
    }

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<StudyResponse>> getAllStudy(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<StudyResponse> studyResponsePage = studyService.getAllStudyMaterial(page, size);
        List<StudyResponse> studyResponses = studyResponsePage.getContent();

        return WebResponse.<List<StudyResponse>>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(studyResponses)
            .paging(PagingResponse.builder()
                .currentPage(page)
                .totalPage(studyResponsePage.getTotalPages())
                .size(size)
                .build())
            .build();
    }

    @DeleteMapping(
        path = "/delete/{studyCode}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteStudy(@PathVariable("studyCode") String studyCode) {
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
    public WebResponse<StudyResponse> updateStudy(
        @PathVariable("studyCode") String studyCode,
        @RequestBody UpdateStudyRequest request
    ) {
        StudyResponse studyResponse = studyService.updateStudy(studyCode, request);

        return WebResponse.<StudyResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(studyResponse)
            .build();
    }

}
