package com.taskmaster.taskmaster.model.request;

import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateNewStudyRequest {

    @NotBlank(message = "Code is required")
    @Size(max = 13, message = "Study Code should be 13 characters")
    private String code;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Study Name should be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "price must be at least 0")
    private int price;

    @NotNull(message = "Discount is required")
    @Min(value = 0, message = "discount must be at least 0")
    private Integer discount;

    private String description;

    @NotBlank(message = "link is required")
    @Size(max = 200, message = "Link should be 200 characters or less")
    private String link;

    @NotNull(message = "Category cannot be null")
    private StudyCategory category;

    @NotNull(message = "Type cannot be null")
    private StudyType type;

    @NotNull(message = "Level cannot be null")
    private StudyLevel level;

}
