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

    @Size(min = 3, max = 100, message = "Study Name should be between 3 and 100 characters")
    private String name;

    @Min(value = 0, message = "price must be at least 0")
    private Integer price;

    @Min(value = 0, message = "discount must be at least 0")
    private Integer discount;

    private String description;

    @Size(max = 200, message = "Link should be 200 characters or less")
    private String link;

    private StudyCategory category;

    private StudyType type;

    private StudyLevel level;

}
