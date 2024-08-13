package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateQuestionsRequest {

    private String questionText;

    private String imageUrl;

    private String explanation;

    private List<UpdateAnswerRequest> answers;

}
