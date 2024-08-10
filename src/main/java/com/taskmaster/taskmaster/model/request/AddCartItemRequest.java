package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCartItemRequest {

    @NotBlank(message = "Code is required")
    @Size(max = 13, message = "Study Code should be 13 characters")
    private String studyCode;

    @NotNull(message = "Quantity must be set and cannot be null")
    @Max(value = 1, message = "Quantity must be exactly 1")
    @Min(value = 1, message = "Quantity must be exactly 1")
    private int quantity;

}
