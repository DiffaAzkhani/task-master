package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddQuestionRequest {

    @NotNull(message = "Study Id is required and cannot null")
    private Long studyId;

    @NotBlank(message = "Study Code is required")
    @Size(max = 200, message = "Username should not be more than 200 characters")
    private String questionText;

    @NotBlank(message = "Image URL Code is required")
    private String imageUrl;

    @NotBlank(message = "Explanation Code is required")
    private String explanation;

    @Valid
    private List<AddAnswerRequest> answers;

}
