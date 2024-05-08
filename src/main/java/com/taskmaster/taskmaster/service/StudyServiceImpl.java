package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.model.request.AddStudyRequest;
import com.taskmaster.taskmaster.model.response.StudyResponse;
import com.taskmaster.taskmaster.repository.StudyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@AllArgsConstructor
@Service
public class StudyServiceImpl implements StudyService {

    private final StudyRepository studyRepository;

    @Override
    public StudyResponse addStudy(AddStudyRequest request) {
        if (Boolean.TRUE.equals(studyRepository.existsByCode(request.getCode()))){
            log.info("Study with code : {}, already exists",request.getCode());
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Study with code : " + request.getCode() + ", already exists"
            );
        }

        if (Boolean.TRUE.equals(studyRepository.existsByNameOrLink(request.getName(), request.getLink()))){
            log.info("Study with name : {}, or Link : {}, already exists",request.getName(), request.getLink());
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Study with name or link already exists"
            );
        }

        if (request.getPrice() == 0.0 && request.getType().equals(StudyType.PREMIUM)) {
            request.setType(StudyType.FREE);
        }

        if (request.getPrice() > 0.0 && request.getType().equals(StudyType.FREE)) {
            request.setType(StudyType.PREMIUM);
        }

        Study study = Study.builder()
            .code(request.getCode())
            .name(request.getName())
            .price(request.getPrice())
            .description(request.getDescription())
            .link(request.getLink())
            .category(request.getCategory())
            .type(request.getType())
            .level(request.getLevel())
            .build();

        studyRepository.save(study);
        log.info("learning material saved successfully");

        return toStudyResponse(study);
    }

    public static StudyResponse toStudyResponse(Study study) {
        return StudyResponse.builder()
            .code(study.getCode())
            .name(study.getName())
            .price(study.getPrice())
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
