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
public class GetQuestionExplanationResponse {

    private String username;

    private String studyCode;

    private List<GetExplanationResponse> explanationResponseList;

    private String userScore;

}
