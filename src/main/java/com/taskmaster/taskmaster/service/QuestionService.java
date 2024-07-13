package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddQuestionRequest;
import com.taskmaster.taskmaster.model.response.GetQuestionResponse;
import com.taskmaster.taskmaster.model.response.QuestionResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {

    List<QuestionResponse> addQuestionAndAnswer(AddQuestionRequest request);

    Page<GetQuestionResponse> getQuestionForStudy(Long studyId, int page, int size);

}
