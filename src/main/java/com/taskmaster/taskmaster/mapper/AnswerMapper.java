package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.entity.Answer;
import com.taskmaster.taskmaster.model.response.AnswerResponse;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {

    public AnswerResponse toAnswerResponse(Answer answer) {
        return AnswerResponse.builder()
            .answerId(answer.getId())
            .answerText(answer.getAnswerText())
            .isCorrect(answer.getIsCorrect())
            .build();
    }

}
