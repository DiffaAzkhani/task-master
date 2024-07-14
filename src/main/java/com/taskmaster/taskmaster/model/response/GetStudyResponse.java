package com.taskmaster.taskmaster.model.response;

import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetStudyResponse {

    private String code;

    private String name;

    private Double price;

    private Integer discount;

    private String description;

    private String link;

    private StudyCategory category;

    private StudyType type;

    private StudyLevel level;

}
