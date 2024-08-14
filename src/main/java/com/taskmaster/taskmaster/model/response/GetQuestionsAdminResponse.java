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
public class GetQuestionsAdminResponse {

    private Long studyId;

    private Long questionId;

    private String questionText;

    private String imageUrl;

    private String explanation;

    private List<AnswerOptionAdminResponse> answers;

}
