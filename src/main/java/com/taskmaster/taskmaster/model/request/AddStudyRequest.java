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
public class AddStudyRequest {

    @NotBlank
    @Size(max = 13)
    private String code;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @Min(0)
    private Double price;

    private String description;

    @NotBlank
    @Size(max = 200)
    private String link;

    @NotNull
    private StudyCategory category;

    @NotNull
    private StudyType type;

    @NotNull
    private StudyLevel level;

}
