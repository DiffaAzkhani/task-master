package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.model.response.StudyResponse;
import org.springframework.stereotype.Component;

@Component
public class StudyMapper {

    public StudyResponse toStudyResponse(Study study) {
        return StudyResponse.builder()
            .code(study.getCode())
            .name(study.getName())
            .price(study.getPrice())
            .discount(study.getDiscount())
            .description(study.getDescription())
            .link(study.getLink())
            .category(study.getCategory())
            .type(study.getType())
            .level(study.getLevel())
            .createdAt(TimeUtil.formatToString(study.getCreatedAt()))
            .updatedAt(TimeUtil.formatToString(study.getCreatedAt()))
            .build();
    }

}
