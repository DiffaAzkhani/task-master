package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreeItemsDetailRequest {

    @NotBlank(message = "Study Code is required")
    @Size(max = 13, message = "Study Code should be 13 characters")
    private String studyCode;

}
