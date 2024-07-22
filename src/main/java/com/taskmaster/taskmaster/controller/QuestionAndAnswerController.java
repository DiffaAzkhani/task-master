package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.AddQuestionRequest;
import com.taskmaster.taskmaster.model.response.AddQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetAllQuestionResponse;
import com.taskmaster.taskmaster.model.response.PagingResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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
        path = "/add-question",
        produces = MediaType.APPLICATION_JSON_VALUE

    )
    public WebResponse<List<AddQuestionResponse>> addQuestionAndAnswer(
        @Valid @RequestBody AddQuestionRequest request
    ) {
        List<AddQuestionResponse> questionResponses = questionService.addQuestionAndAnswer(request);

        return WebResponse.<List<AddQuestionResponse>>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(questionResponses)
            .build();
    }

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<GetAllQuestionResponse>> getQuestionAndAnswer(
        @RequestParam(name = "studyId") Long studyId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "1") int size
    ) {
        Page<GetAllQuestionResponse> questionResponsePage = questionService.getQuestionForStudy(studyId, page, size);
        List<GetAllQuestionResponse> questionResponses = questionResponsePage.getContent();

        return WebResponse.<List<GetAllQuestionResponse>>builder()
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

}