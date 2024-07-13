package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.entity.Question;
import com.taskmaster.taskmaster.model.response.GetQuestionResponse;
import com.taskmaster.taskmaster.model.response.QuestionResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class QuestionMapper {

    private final AnswerMapper answerMapper = new AnswerMapper();

    public QuestionResponse toQuestionResponse(Question question) {
        return QuestionResponse.builder()
            .studyId(question.getStudy().getId())
            .id(question.getId())
            .questionText(question.getQuestionText())
            .imageUrl(question.getImageUrl())
            .explanation(question.getExplanation())
            .answers(question.getAnswers().stream()
                .map(answerMapper::toAnswerResponse)
                .collect(Collectors.toList()))
            .build();
    }

    public GetQuestionResponse toGetQuestionResponse(Question question) {
        return GetQuestionResponse.builder()
            .studyId(question.getStudy().getId())
            .id(question.getId())
            .questionText(question.getQuestionText())
            .imageUrl(question.getImageUrl())
            .answers(question.getAnswers().stream()
                .map(answerMapper::toAnswerOptionResponse)
                .collect(Collectors.toList()))
            .build();
    }

}
