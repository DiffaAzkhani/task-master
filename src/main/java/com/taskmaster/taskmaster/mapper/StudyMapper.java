package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.model.response.AddStudyResponse;
import com.taskmaster.taskmaster.model.response.GetAllStudyResponse;
import com.taskmaster.taskmaster.model.response.GetStudyResponse;
import com.taskmaster.taskmaster.model.response.UpdateStudyResponse;
import org.springframework.stereotype.Component;

@Component
public class StudyMapper {

    public AddStudyResponse toAddStudyResponse(Study study) {
        return AddStudyResponse.builder()
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

    public GetStudyResponse toGetStudyResponse(Study study) {
        return GetStudyResponse.builder()
            .code(study.getCode())
            .name(study.getName())
            .price(study.getPrice())
            .discount(study.getDiscount())
            .description(study.getDescription())
            .link(study.getLink())
            .category(study.getCategory())
            .type(study.getType())
            .level(study.getLevel())
            .build();
    }

    public GetAllStudyResponse toGetAllStudyResponse(Study study) {
        return GetAllStudyResponse.builder()
            .code(study.getCode())
            .name(study.getName())
            .price(study.getPrice())
            .discount(study.getDiscount())
            .description(study.getDescription())
            .link(study.getLink())
            .category(study.getCategory())
            .type(study.getType())
            .level(study.getLevel())
            .build();
    }

    public UpdateStudyResponse toUpdateStudyResponse(Study study) {
        return UpdateStudyResponse.builder()
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
