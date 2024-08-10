package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.entity.Question;
import com.taskmaster.taskmaster.entity.UserAnswer;
import com.taskmaster.taskmaster.entity.UserGrade;
import com.taskmaster.taskmaster.model.response.*;
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

    public GetQuestionsResponse toGetQuestionResponse(Question question) {
        return GetQuestionsResponse.builder()
            .studyId(question.getStudy().getId())
            .questionId(question.getId())
            .questionText(question.getQuestionText())
            .imageUrl(question.getImageUrl())
            .answers(question.getAnswers().stream()
                .map(answerMapper::toAnswerOptionResponse)
                .collect(Collectors.toList()))
            .build();
    }

    public GetExplanationResponse toGetAllExplanationResponse(List<UserAnswer> userAnswerList, UserGrade userGrade) {
        List<GetQuestionResponse> explanationList = userAnswerList.stream()
            .map(questions -> GetQuestionResponse.builder()
                .questionId(String.valueOf(questions.getId()))
                .questionText(questions.getQuestion().getQuestionText())
                .imageUrl(questions.getQuestion().getImageUrl())
                .explanation(questions.getQuestion().getExplanation())
                .answerId(questions.getAnswer().getId())
                .answerText(questions.getAnswer().getAnswerText())
                .isCorrect(questions.getAnswer().getIsCorrect())
                .build())
            .collect(Collectors.toList());

        return GetExplanationResponse.builder()
            .userScore(String.valueOf(userGrade.getScore()))
            .explanationList(explanationList)
            .build();
    }

}
