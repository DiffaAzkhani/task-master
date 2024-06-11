package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.model.request.AddStudyRequest;
import com.taskmaster.taskmaster.model.request.UpdateStudyRequest;
import com.taskmaster.taskmaster.model.response.StudyResponse;
import com.taskmaster.taskmaster.repository.StudyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        log.info("learning material saved successfully in data : {}", study);

        return toStudyResponse(study);
    }

    @Override
    public StudyResponse getStudy(String code) {
        Study study = studyRepository.findByCode(code)
            .orElseThrow(() -> {
                log.warn("Study with code : {}, not found!", code);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,"Study not found!");
            });

        log.info("found study code : {}", study);

        return toStudyResponse(study);
    }

    @Override
    public Page<StudyResponse> getAllStudyMaterial(int page, int size) {
        log.info("Fetching all available courses. Page: {}, Size: {}", page, size);

        PageRequest request = PageRequest.of(page, size);
        Page<Study> studyPage = studyRepository.findAll(request);

        List<StudyResponse> studyResponses = studyPage.getContent().stream()
            .map(StudyServiceImpl::toStudyResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(studyResponses, request, studyPage.getTotalElements());
    }

    @Override
    public void deleteStudy(String code) {
        Study study = studyRepository.findByCode(code)
            .orElseThrow(() -> {
                log.warn("Study with code : {}, not found!", code);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found!");
            });

        log.info("found study code : {}", study);

        studyRepository.delete(study);

        log.info("Study deleted successfully!");
    }

    @Override
    public StudyResponse updateStudy(String code, UpdateStudyRequest request) {
        Study study = studyRepository.findByCode(code)
            .orElseThrow(() -> {
                log.warn("Study with code : {}, not found!", code);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,"Study not found!");
            });

        updateStudyProperties(study, request);

        studyRepository.save(study);
        log.info("Study successfully updated!");

        return toStudyResponse(study);
    }

    private void updateStudyProperties(Study study, UpdateStudyRequest request) {
        if (Objects.nonNull(request.getName())) {
            study.setName(request.getName());
            log.info("Updated course name to : {}", request.getName());
        }

        if (Objects.nonNull(request.getPrice())) {
            study.setPrice(request.getPrice());
            log.info("Updated course price to : {}", request.getPrice());
        }

        if (Objects.nonNull(request.getDescription())) {
            study.setDescription(request.getDescription());
            log.info("Updated course description to : {}", request.getDescription());
        }

        if (Objects.nonNull(request.getLink())) {
            study.setLink(request.getLink());
            log.info("Updated course link to : {}", request.getLink());
        }

        if (Objects.nonNull(request.getLevel())) {
            study.setLevel(request.getLevel());
            log.info("Updated course level to : {}", request.getLevel());
        }

        if (Objects.nonNull(request.getCategory())) {
            study.setCategory(request.getCategory());
            log.info("Updated course category to : {}", request.getCategory());
        }

        if (Objects.nonNull(request.getType())) {
            study.setType(request.getType());
            log.info("Updated course type to : {}", request.getType());
        }
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
