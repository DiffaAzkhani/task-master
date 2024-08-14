package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyFilter;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.mapper.StudyMapper;
import com.taskmaster.taskmaster.model.request.CreateNewStudyRequest;
import com.taskmaster.taskmaster.model.request.UpdateStudyRequest;
import com.taskmaster.taskmaster.model.response.CreateNewStudyResponse;
import com.taskmaster.taskmaster.model.response.GetStudiesResponse;
import com.taskmaster.taskmaster.model.response.GetStudyByIdResponse;
import com.taskmaster.taskmaster.model.response.UpdateStudyResponse;
import com.taskmaster.taskmaster.repository.StudyRepository;
import com.taskmaster.taskmaster.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class StudyServiceImpl implements StudyService {

    private final StudyRepository studyRepository;

    private final UserRepository userRepository;

    private final StudyMapper studyMapper;

    private final ValidationService validationService;

    @Override
    @Transactional
    public CreateNewStudyResponse createNewStudy(CreateNewStudyRequest request) {
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
            .discount(request.getDiscount())
            .description(request.getDescription())
            .link(request.getLink())
            .category(request.getCategory())
            .type(request.getType())
            .level(request.getLevel())
            .build();

        studyRepository.save(study);
        log.info("learning material saved successfully in data : {}", study);

        return studyMapper.toAddStudyResponse(study);
    }

    @Override
    @Transactional(readOnly = true)
    public GetStudyByIdResponse getStudyById(Long studyId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> {
                log.warn("Study with id: {}, not found!", studyId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,"Study not found!");
            });

        log.info("found study:{}", study);

        return studyMapper.toGetStudyResponse(study);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetStudiesResponse> getAllStudies(StudyType studyType,
                                                  Set<StudyCategory> studyCategories,
                                                  Set<StudyLevel> studyLevels,
                                                  StudyFilter studyFilters,
                                                  Integer minPrice,
                                                  Integer maxPrice,
                                                  int page,
                                                  int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Study> studyPage = studyRepository.findByFilters(studyType, studyCategories, studyLevels, minPrice, maxPrice, pageRequest);
        List<Study> filteredSudy = applyFilterByEnum(studyPage.getContent(), studyFilters);

        List<GetStudiesResponse> responses = filteredSudy.stream()
            .map(studyMapper::toGetAllStudyResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(responses, pageRequest, studyPage.getTotalElements());
    }

    private List<Study> applyFilterByEnum(List<Study> studies,
                                          StudyFilter studyFilters) {
        if (studyFilters == null) {
            return studies;
        }

        switch (studyFilters) {
            case POPULAR:
                return applyFilterByPopular(studies);
            case RECENTLY_ADDED :
                return applyFilterByRecentlyAdded(studies);
            case DISCOUNT:
                return applyFilterByDiscount(studies);
            default:
                return studies;
        }
    }

    private List<Study> applyFilterByDiscount(List<Study> studies) {
        return studies.stream()
            .filter(study -> study.getDiscount() != null && study.getDiscount() > 0)
            .collect(Collectors.toList());
    }

    private List<Study> applyFilterByPopular(List<Study> studies) {
        return studies.stream()
            .filter(study -> !study.getUsers().isEmpty())
            .sorted((study1, study2) -> Integer.compare(study2.getUsers().size(), study1.getUsers().size()))
            .collect(Collectors.toList());
    }

    private List<Study> applyFilterByRecentlyAdded(List<Study> studies) {
        return studies.stream()
            .sorted(Comparator.comparing((Study::getCreatedAt)).reversed())
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteStudy(Long studyId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> {
                log.warn("Study with id: {}, not found!", studyId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found!");
            });

        log.info("found study code : {}", study);

        studyRepository.delete(study);

        log.info("Study deleted successfully!");
    }

    @Override
    @Transactional
    public UpdateStudyResponse updateStudy(Long studyId, UpdateStudyRequest request) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> {
                log.warn("Study with id: {}, not found!", studyId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,"Study not found!");
            });

        updateStudyProperties(study, request);

        studyRepository.save(study);
        log.info("Study successfully updated!");

        return studyMapper.toUpdateStudyResponse(study);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetStudiesResponse> getMyStudies(int page, int size) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Study> studyPage = studyRepository.findByUsers_Username(currentUser, pageRequest);
        List<GetStudiesResponse> getMyStudiesResponses = studyPage.getContent().stream()
            .map(studyMapper::toGetAllStudyResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(getMyStudiesResponses ,pageRequest, studyPage.getTotalElements());
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

        if (Objects.nonNull(request.getDiscount())) {
            study.setDiscount(request.getDiscount());
            log.info("Updated course discount to : {}", request.getDiscount());
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

}
