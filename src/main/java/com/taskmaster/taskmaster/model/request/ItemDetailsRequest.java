package com.taskmaster.taskmaster.model.request;

import com.taskmaster.taskmaster.enums.StudyCategory;
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
public class ItemDetailsRequest {

    @NotNull(message = "Study id is required")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Study Name should be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "price must be at least 0")
    private int price;

    @NotNull(message = "Quantity must be set and cannot be null")
    @Max(value = 1, message = "Quantity must be exactly 1")
    @Min(value = 1, message = "Quantity must be exactly 1")
    private int quantity;

    @NotNull(message = "Category cannot be null")
    private StudyCategory category;

}
