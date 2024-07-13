package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.entity.Answer;
import com.taskmaster.taskmaster.model.response.AnswerOptionResponse;
import com.taskmaster.taskmaster.model.response.AnswerResponse;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {

    public AnswerResponse toAnswerResponse(Answer answer) {
        return AnswerResponse.builder()
            .id(answer.getId())
            .answerText(answer.getAnswerText())
            .isCorrect(answer.getIsCorrect())
            .build();
    }

    public AnswerOptionResponse toAnswerOptionResponse(Answer answer) {
        return AnswerOptionResponse.builder()
            .id(answer.getId())
            .answerText(answer.getAnswerText())
            .build();
    }

}
