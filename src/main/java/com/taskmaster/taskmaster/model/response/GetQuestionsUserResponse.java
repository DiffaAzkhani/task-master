package com.taskmaster.taskmaster.model.response;

import com.taskmaster.taskmaster.model.request.AnswerOptionUserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetQuestionsUserResponse {

    private Long studyId;

    private Long questionId;

    private String questionText;

    private String imageUrl;

    private List<AnswerOptionUserResponse> answers;

}
