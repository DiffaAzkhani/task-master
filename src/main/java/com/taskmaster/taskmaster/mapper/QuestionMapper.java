package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.entity.Question;
import com.taskmaster.taskmaster.entity.UserAnswer;
import com.taskmaster.taskmaster.entity.UserGrade;
import com.taskmaster.taskmaster.model.response.AddQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetAllQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionExplanationResponse;
import com.taskmaster.taskmaster.model.response.GetExplanationResponse;
import org.springframework.stereotype.Component;

import java.util.List;
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

    public GetQuestionExplanationResponse toGetAllQuestionExplanationResponse(List<UserAnswer> userAnswer, UserGrade userGrade) {
        List<GetExplanationResponse> explanationResponseList = userAnswer.stream()
            .map(questionExplanation -> GetExplanationResponse.builder()
                .questionText(questionExplanation.getQuestion().getQuestionText())
                .explanation(questionExplanation.getQuestion().getExplanation())
                .userAnswer(questionExplanation.getAnswer().getAnswerText())
                .isCorrect(questionExplanation.getAnswer().getIsCorrect())
                .imageUrl(questionExplanation.getQuestion().getImageUrl())
                .build())
            .collect(Collectors.toList());

        return GetQuestionExplanationResponse.builder()
            .username(userGrade.getUser().getUsername())
            .studyCode(userGrade.getStudy().getCode())
            .explanationResponseList(explanationResponseList)
            .userScore(String.valueOf(userGrade.getScore()))
            .build();
    }

}
