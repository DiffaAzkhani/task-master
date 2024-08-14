package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.AddQuestionRequest;
import com.taskmaster.taskmaster.model.request.AnswerSubmissionRequest;
import com.taskmaster.taskmaster.model.request.UpdateQuestionsRequest;
import com.taskmaster.taskmaster.model.response.AddQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetExplanationResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionsAdminResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionsUserResponse;
import com.taskmaster.taskmaster.model.response.GradeSubmissionResponse;
import com.taskmaster.taskmaster.model.response.PagingResponse;
import com.taskmaster.taskmaster.model.response.PagingWebResponse;
import com.taskmaster.taskmaster.model.response.UpdateQuestionsResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public PagingWebResponse<List<GetQuestionsAdminResponse>> getQuestionAndAnswerForAdmin(
        @RequestParam(name = "studyId") Long studyId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetQuestionsAdminResponse> questionResponsePage = questionService.getQuestionAndAnswerForAdmin(studyId, page, size);
        List<GetQuestionsAdminResponse> questionResponses = questionResponsePage.getContent();

        return PagingWebResponse.<List<GetQuestionsAdminResponse>>builder()
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

    @PatchMapping(
        path = "/questions/{questionId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UpdateQuestionsResponse> updateQuestionForAdmin(
        @PathVariable(name = "questionId") Long questionId,
        @Valid @RequestBody UpdateQuestionsRequest request
    ) {
        UpdateQuestionsResponse questionsResponse = questionService.updateQuestionsForAdmin(questionId, request);

        return WebResponse.<UpdateQuestionsResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(questionsResponse)
            .build();
    }

    @DeleteMapping(
        path = "/questions/{questionId}"
    )
    public WebResponse<String> deleteQuestion(
        @PathVariable(name = "questionId") Long questionId
    ) {
        questionService.deleteQuestion(questionId);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @DeleteMapping(
        path = "/answers/study/{studyId}/users/{userId}"
    )
    public WebResponse<String> deleteUserAnswerForAdmin(
        @PathVariable(name = "studyId") Long studyId,
        @PathVariable(name = "userId") Long userId
    ) {
        questionService.deleteUserAnswerForAdmin(studyId, userId);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @GetMapping(
        path = "/questions/me/{studyId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PagingWebResponse<List<GetQuestionsUserResponse>> getQuestionAndAnswerForUser(
        @RequestParam(name = "studyId") Long studyId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetQuestionsUserResponse> questionResponsePage = questionService.getQuestionAndAnswerForUser(studyId, page, size);
        List<GetQuestionsUserResponse> questionResponses = questionResponsePage.getContent();

        return PagingWebResponse.<List<GetQuestionsUserResponse>>builder()
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
        @PathVariable(name = "studyId") Long studyId
    ) {
        GradeSubmissionResponse gradeSubmissionResponse = questionService.gradeSubmission(studyId);

        return WebResponse.<GradeSubmissionResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(gradeSubmissionResponse)
            .build();
    }

    @GetMapping(
        path = "/answers/me/study/{studyId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<GetExplanationResponse> getExplanationAndUserAnswer(
        @RequestParam(name = "studyId") Long studyId
    ) {
        GetExplanationResponse explanationResponses = questionService.getExplanationAndUserAnswer(studyId);

        return WebResponse.<GetExplanationResponse>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(explanationResponses)
            .build();
    }

    @DeleteMapping(
        path = "/answers/me/studies/{studyId}"
    )
    public WebResponse<String> deleteUserAnswer(
        @PathVariable(name = "studyId") Long studyId
    ) {
        questionService.deleteUserAnswer(studyId);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

}
