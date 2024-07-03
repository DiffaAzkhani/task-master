package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddQuestionRequest;
import com.taskmaster.taskmaster.model.response.QuestionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionService {

    List<QuestionResponse> addQuestionAndAnswer(AddQuestionRequest request);

}
