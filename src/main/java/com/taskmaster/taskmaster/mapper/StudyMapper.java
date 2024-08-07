package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.model.response.CreateNewStudyResponse;
import com.taskmaster.taskmaster.model.response.GetAllStudiesResponse;
import com.taskmaster.taskmaster.model.response.GetStudyByCodeResponse;
import com.taskmaster.taskmaster.model.response.UpdateStudyResponse;
import org.springframework.stereotype.Component;

@Component
public class StudyMapper {

    public CreateNewStudyResponse toAddStudyResponse(Study study) {
        return CreateNewStudyResponse.builder()
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

    public GetStudyByCodeResponse toGetStudyResponse(Study study) {
        return GetStudyByCodeResponse.builder()
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

    public GetAllStudiesResponse toGetAllStudyResponse(Study study) {
        return GetAllStudiesResponse.builder()
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
