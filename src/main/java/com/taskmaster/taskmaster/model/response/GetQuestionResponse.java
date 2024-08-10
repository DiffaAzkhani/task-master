package com.taskmaster.taskmaster.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetQuestionResponse {

    private String questionId;

    private String questionText;

    private String explanation;

    private String imageUrl;

    private Long answerId;

    private String answerText;

    private boolean isCorrect;


}
