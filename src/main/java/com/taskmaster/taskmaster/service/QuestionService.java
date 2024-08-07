package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddQuestionRequest;
import com.taskmaster.taskmaster.model.request.AnswerSubmissionRequest;
import com.taskmaster.taskmaster.model.response.AddQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetAllQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionExplanationResponse;
import com.taskmaster.taskmaster.model.response.GradeSubmissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {

    List<AddQuestionResponse> createQuestionAndAnswer(AddQuestionRequest request);

    Page<GetAllQuestionResponse> getQuestionForStudy(Long studyId, int page, int size);

    void answerSubmission(Long studyId, AnswerSubmissionRequest request);

    GradeSubmissionResponse gradeSubmission(Long studyId, String username);

    GetQuestionExplanationResponse getExplanationAndUserAnswer(String username, Long studyId);

}
