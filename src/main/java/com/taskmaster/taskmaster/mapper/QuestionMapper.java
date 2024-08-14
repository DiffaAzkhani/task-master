package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Question;
import com.taskmaster.taskmaster.entity.UserAnswer;
import com.taskmaster.taskmaster.entity.UserGrade;
import com.taskmaster.taskmaster.model.request.AnswerOptionUserResponse;
import com.taskmaster.taskmaster.model.request.UpdateAnswerRequest;
import com.taskmaster.taskmaster.model.response.AddQuestionResponse;
import com.taskmaster.taskmaster.model.response.AnswerOptionAdminResponse;
import com.taskmaster.taskmaster.model.response.GetExplanationResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionsAdminResponse;
import com.taskmaster.taskmaster.model.response.GetQuestionsUserResponse;
import com.taskmaster.taskmaster.model.response.UpdateQuestionsResponse;
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

    public GetQuestionsUserResponse toGetQuestionForUserResponse(Question question) {
        List<AnswerOptionUserResponse> answerList = question.getAnswers().stream()
            .map(answer -> AnswerOptionUserResponse.builder()
                .answerId(answer.getId())
                .answerText(answer.getAnswerText())
                .build())
            .collect(Collectors.toList());

        return GetQuestionsUserResponse.builder()
            .studyId(question.getStudy().getId())
            .questionId(question.getId())
            .questionText(question.getQuestionText())
            .imageUrl(question.getImageUrl())
            .answers(answerList)
            .build();
    }

    public GetQuestionsAdminResponse toGetQuestionForAdminResponse(Question question) {
        List<AnswerOptionAdminResponse> answerList = question.getAnswers().stream()
            .map(answer -> AnswerOptionAdminResponse.builder()
                .answerId(answer.getId())
                .answerText(answer.getAnswerText())
                .isCorrect(answer.getIsCorrect())
                .build())
            .collect(Collectors.toList());

        return GetQuestionsAdminResponse.builder()
            .studyId(question.getStudy().getId())
            .questionId(question.getId())
            .questionText(question.getQuestionText())
            .imageUrl(question.getImageUrl())
            .explanation(question.getExplanation())
            .answers(answerList)
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

    public UpdateQuestionsResponse toUpdateQuestionResponse(Question question) {
        List<UpdateAnswerRequest> answerRequestResponseList = question.getAnswers().stream()
            .map(answer -> UpdateAnswerRequest.builder()
                .answerId(answer.getId())
                .answerText(answer.getAnswerText())
                .isCorrect(answer.getIsCorrect())
                .build())
            .collect(Collectors.toList());

        return UpdateQuestionsResponse.builder()
            .questionText(question.getQuestionText())
            .explanation(question.getExplanation())
            .imageUrl(question.getImageUrl())
            .createdAt(TimeUtil.formatToString(question.getCreatedAt()))
            .updatedAt(TimeUtil.formatToString(question.getUpdatedAt()))
            .answerRequests(answerRequestResponseList)
            .build();
    }

}
