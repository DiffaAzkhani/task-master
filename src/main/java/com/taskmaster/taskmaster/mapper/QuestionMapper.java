package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.entity.Question;
import com.taskmaster.taskmaster.model.response.AddQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetAllQuestionResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class QuestionMapper {

    private final AnswerMapper answerMapper = new AnswerMapper();

    public AddQuestionResponse toAddQuestionResponse(Question question) {
        return AddQuestionResponse.builder()
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

    public GetAllQuestionResponse toGetQuestionResponse(Question question) {
        return GetAllQuestionResponse.builder()
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
