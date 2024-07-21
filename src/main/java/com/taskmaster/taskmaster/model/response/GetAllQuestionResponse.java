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
public class GetAllQuestionResponse {

    private Long studyId;

    private Long id;

    private String questionText;

    private String imageUrl;

    private List<AnswerOptionResponse> answers;

}
