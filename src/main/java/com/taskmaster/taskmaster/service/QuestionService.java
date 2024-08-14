package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddQuestionRequest;
import com.taskmaster.taskmaster.model.request.AnswerSubmissionRequest;
import com.taskmaster.taskmaster.model.request.UpdateQuestionsRequest;
import com.taskmaster.taskmaster.model.request.UpdateUserAnswerRequest;
import com.taskmaster.taskmaster.model.response.AddQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetExplanationResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionsAdminResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionsUserResponse;
import com.taskmaster.taskmaster.model.response.GradeSubmissionResponse;
import com.taskmaster.taskmaster.model.response.UpdateQuestionsResponse;
import com.taskmaster.taskmaster.model.response.UpdateUserAnswerResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {

    List<AddQuestionResponse> createQuestionAndAnswer(AddQuestionRequest request);

    Page<GetQuestionsUserResponse> getQuestionAndAnswerForUser(Long studyId, int page, int size);

    Page<GetQuestionsAdminResponse> getQuestionAndAnswerForAdmin(Long studyId, int page, int size);

    void answerSubmission(Long studyId, AnswerSubmissionRequest request);

    GradeSubmissionResponse gradeSubmission(Long studyId);

    GetExplanationResponse getExplanationAndUserAnswer(Long studyId);

    UpdateQuestionsResponse updateQuestionsForAdmin(Long questionId, UpdateQuestionsRequest request);

    void deleteQuestion(Long questionId);

    void deleteUserAnswer(Long studyId);

    void deleteUserAnswerForAdmin(Long studyId, Long userId);

    UpdateUserAnswerResponse updateUserAnswerResponse(List<UpdateUserAnswerRequest> request, Long studyId);

}
