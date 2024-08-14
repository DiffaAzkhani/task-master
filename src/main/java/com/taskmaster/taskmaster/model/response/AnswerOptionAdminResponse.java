package com.taskmaster.taskmaster.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerOptionAdminResponse {

    private Long answerId;

    private String answerText;

    private boolean isCorrect;

}
