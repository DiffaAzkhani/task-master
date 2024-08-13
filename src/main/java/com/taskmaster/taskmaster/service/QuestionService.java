package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddQuestionRequest;
import com.taskmaster.taskmaster.model.request.AnswerSubmissionRequest;
import com.taskmaster.taskmaster.model.request.UpdateQuestionsRequest;
import com.taskmaster.taskmaster.model.response.AddQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetExplanationResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionsResponse;
import com.taskmaster.taskmaster.model.response.GradeSubmissionResponse;
import com.taskmaster.taskmaster.model.response.UpdateQuestionsResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {

    List<AddQuestionResponse> createQuestionAndAnswer(AddQuestionRequest request);

    Page<GetQuestionsResponse> getQuestionAndAnswerForUser(Long studyId, int page, int size);

    Page<GetQuestionsResponse> getQuestionAndAnswerForAdmin(Long studyId, int page, int size);

    void answerSubmission(Long studyId, AnswerSubmissionRequest request);

    GradeSubmissionResponse gradeSubmission(Long studyId);

    GetExplanationResponse getExplanationAndUserAnswer(Long studyId);

    UpdateQuestionsResponse updateQuestionsForAdmin(Long questionId, UpdateQuestionsRequest request);

}
