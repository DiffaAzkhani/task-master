package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.model.response.CreateNewStudyResponse;
import com.taskmaster.taskmaster.model.response.GetAllEnrolledUSerStudyResponse;
import com.taskmaster.taskmaster.model.response.GetAllStudiesResponse;
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

    public GetAllEnrolledUSerStudyResponse toGetEnrolledUserStudyResponse(Study study) {
        return GetAllEnrolledUSerStudyResponse.builder()
            .code(study.getCode())
            .studyName(study.getName())
            .type(study.getType())
            .level(study.getLevel())
            .category(study.getCategory())
            .build();
    }

}
