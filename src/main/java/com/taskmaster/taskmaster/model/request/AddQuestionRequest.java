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
public class AddQuestionRequest {

    @NotNull
    private Long studyId;

    @NotBlank
    @Size(max = 255)
    private String questionText;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String explanation;

    private List<AddAnswerRequest> answers;

}
