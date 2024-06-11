package com.taskmaster.taskmaster.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponse {

    private Long studyId;

    private String questionText;

    private String imageUrl;

    private String explanation;

    private List<AnswerResponse> answers;

}
