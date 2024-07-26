package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerSubmission {

    @NotNull(message = "Answer Option ID cannot be null")
    private Long questionId;

    @NotNull(message = "Answer Option ID cannot be null")
    private Long answerId;

}
