package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.AddQuestionRequest;
import com.taskmaster.taskmaster.model.request.AnswerSubmissionRequest;
import com.taskmaster.taskmaster.model.response.AddQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetAllQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionExplanationResponse;
import com.taskmaster.taskmaster.model.response.GradeSubmissionResponse;
import com.taskmaster.taskmaster.model.response.PagingResponse;
import com.taskmaster.taskmaster.model.response.PagingWebResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/v1/qna")
public class QuestionAndAnswerController {

    private final QuestionService questionService;

    @PostMapping(
        path = "/questions",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AddQuestionResponse>> createQuestion(
        @Valid @RequestBody AddQuestionRequest request
    ) {
        List<AddQuestionResponse> questionResponses = questionService.createQuestionAndAnswer(request);

        return WebResponse.<List<AddQuestionResponse>>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(questionResponses)
            .build();
    }

    @GetMapping(
        path = "/questions/{studyId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PagingWebResponse<List<GetAllQuestionResponse>> getQuestionAndAnswer(
        @RequestParam(name = "studyId") Long studyId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetAllQuestionResponse> questionResponsePage = questionService.getQuestionForStudy(studyId, page, size);
        List<GetAllQuestionResponse> questionResponses = questionResponsePage.getContent();

        return PagingWebResponse.<List<GetAllQuestionResponse>>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(questionResponses)
            .paging(PagingResponse.builder()
                .currentPage(page)
                .size(size)
                .totalPage(questionResponsePage.getTotalPages())
                .totalElement(questionResponsePage.getTotalElements())
                .empty(questionResponses.isEmpty())
                .first(questionResponsePage.isFirst())
                .last(questionResponsePage.isLast())
                .build())
            .build();
    }

    @PostMapping(
        path = "/studies/{studyId}/submission",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> answerSubmission(
        @PathVariable(name = "studyId") Long studyId,
        @Valid @RequestBody AnswerSubmissionRequest request
    ) {
        questionService.answerSubmission(studyId, request);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @PostMapping(
        path = "/studies/{studyId}/grade",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<GradeSubmissionResponse> gradeSubmission(
        @PathVariable(name = "studyId") Long studyId,
        @RequestParam(name = "username") String username
    ) {
        GradeSubmissionResponse gradeSubmissionResponse = questionService.gradeSubmission(studyId, username);

        return WebResponse.<GradeSubmissionResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(gradeSubmissionResponse)
            .build();
    }

    @GetMapping(
        path = "/answers/study/{studyId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<GetQuestionExplanationResponse> getExplanationAndUserAnswer(
        @RequestParam(name = "username") String username,
        @RequestParam(name = "studyId") Long studyId
    ) {
        GetQuestionExplanationResponse explanationResponses = questionService.getExplanationAndUserAnswer(username, studyId);

        return WebResponse.<GetQuestionExplanationResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(explanationResponses)
            .build();
    }

}
