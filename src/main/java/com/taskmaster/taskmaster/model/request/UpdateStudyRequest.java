package com.taskmaster.taskmaster.model.request;

import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStudyRequest {

    @Size(max = 100)
    private String name;

    @Min(0)
    private Double price;

    private String description;

    @Size(max = 200)
    private String link;

    private StudyCategory category;

    private StudyType type;

    private StudyLevel level;

}
