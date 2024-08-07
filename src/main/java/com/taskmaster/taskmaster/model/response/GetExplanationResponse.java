package com.taskmaster.taskmaster.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetExplanationResponse {

    private String questionText;

    private String explanation;

    private String userAnswer;

    private boolean isCorrect;

    private String imageUrl;

}
