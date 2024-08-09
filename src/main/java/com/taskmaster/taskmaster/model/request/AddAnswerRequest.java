package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddAnswerRequest {

    @NotBlank(message = "Answer text is required")
    @Size(max = 100, message = "Answer text should not be more than 100 characters")
    String answerText;

    @NotNull
    Boolean isCorrect;

}
