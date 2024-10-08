package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.model.response.CreateNewStudyResponse;
import com.taskmaster.taskmaster.model.response.GetStudiesResponse;
import com.taskmaster.taskmaster.model.response.GetStudyByIdResponse;
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

    public GetStudyByIdResponse toGetStudyResponse(Study study) {
        return GetStudyByIdResponse.builder()
            .studyId(study.getId())
            .code(study.getCode())
            .studyName(study.getName())
            .price(study.getPrice())
            .discount(study.getDiscount())
            .description(study.getDescription())
            .link(study.getLink())
            .category(study.getCategory())
            .type(study.getType())
            .level(study.getLevel())
            .build();
    }

    public GetStudiesResponse toGetAllStudyResponse(Study study) {
        return GetStudiesResponse.builder()
            .studyId(study.getId())
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
            .studyName(study.getName())
            .price(study.getPrice())
            .discount(study.getDiscount())
            .description(study.getDescription())
            .link(study.getLink())
            .category(study.getCategory())
            .type(study.getType())
            .level(study.getLevel())
            .createdAt(TimeUtil.formatToString(study.getCreatedAt()))
            .updatedAt(TimeUtil.formatToString(study.getUpdatedAt()))
            .build();
    }

}
