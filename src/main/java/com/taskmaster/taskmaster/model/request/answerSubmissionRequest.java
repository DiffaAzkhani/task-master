package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class answerSubmissionRequest {

    @NotBlank(message = "Username is required")
    @Size(max = 100, message = "Username should not be more than 100 characters")
    private String username;

    @NotNull(message = "Study ID is required")
    private Long studyId;

    private List<AnswerSubmission> submissionList;

}
