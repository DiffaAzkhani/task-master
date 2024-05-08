package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.AddStudyRequest;
import com.taskmaster.taskmaster.model.response.StudyResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.StudyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/learning-material")
public class StudyController {

    private final StudyService studyService;

    @PostMapping(
        path = "add-study",
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

}
