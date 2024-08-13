package com.taskmaster.taskmaster.model.response;

import com.taskmaster.taskmaster.entity.Answer;
import com.taskmaster.taskmaster.model.request.UpdateAnswerRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateQuestionsResponse {

    private String questionText;

    private String imageUrl;

    private String explanation;

    private String createdAt;

    private String updatedAt;

    private List<UpdateAnswerRequest> answerRequests;

}
