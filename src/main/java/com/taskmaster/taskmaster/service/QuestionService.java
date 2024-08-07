package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddQuestionRequest;
import com.taskmaster.taskmaster.model.request.GradeSubmissionRequest;
import com.taskmaster.taskmaster.model.request.answerSubmissionRequest;
import com.taskmaster.taskmaster.model.response.AddQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetAllQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionExplanationResponse;
import com.taskmaster.taskmaster.model.response.GradeSubmissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {

    List<AddQuestionResponse> addQuestionAndAnswer(AddQuestionRequest request);

    Page<GetAllQuestionResponse> getQuestionForStudy(Long studyId, int page, int size);

    void answerSubmission(answerSubmissionRequest request);

    GradeSubmissionResponse gradeSubmission(GradeSubmissionRequest request);

    GetQuestionExplanationResponse getExplanationAndUserAnswer(String username, String studyCode);

}
