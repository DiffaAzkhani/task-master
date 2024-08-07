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
import com.taskmaster.taskmaster.model.response.GetAllStudiesResponse;
import com.taskmaster.taskmaster.model.response.GetStudyByCodeResponse;
import com.taskmaster.taskmaster.model.response.UpdateStudyResponse;
import com.taskmaster.taskmaster.repository.StudyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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

    private final StudyMapper studyMapper;


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
    @Transactional
    public GetStudyByCodeResponse getStudyByCode(String code) {
        Study study = studyRepository.findByCode(code)
            .orElseThrow(() -> {
                log.warn("Study with code : {}, not found!", code);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,"Study not found!");
            });

        log.info("found study code : {}", study);

        return studyMapper.toGetStudyResponse(study);
    }

    @Override
    @Transactional
    public Page<GetAllStudiesResponse> getAllStudies(StudyType studyType,
                                                     Set<StudyCategory> studyCategories,
                                                     Set<StudyLevel> studyLevels,
                                                     StudyFilter studyFilters,
                                                     Double minPrice,
                                                     Double maxPrice,
                                                     int page,
                                                     int size) {
        log.info("Fetching all available study. Page: {}, Size: {}", page, size);

        PageRequest pageRequest = PageRequest.of(page, size);
        List<Study> studies = studyRepository.findAll();

        return filteringAndPagingStudy(studyType, studyCategories, studyLevels, pageRequest, studyFilters, minPrice, maxPrice, studies);
    }

    private Page<GetAllStudiesResponse> filteringAndPagingStudy(StudyType studyType,
                                                                Set<StudyCategory> studyCategories,
                                                                Set<StudyLevel> studyLevels,
                                                                PageRequest pageRequest,
                                                                StudyFilter studyFilters,
                                                                Double minPrice,
                                                                Double maxPrice,
                                                                List<Study> studies) {
        List<Study> filteredStudies = filterStudies(studyType, studyCategories, studyLevels, studyFilters, minPrice, maxPrice, studies);
        List<GetAllStudiesResponse> getStudyResponse = paginateAndConvertToResponse(pageRequest, filteredStudies);

        return new PageImpl<>(getStudyResponse, pageRequest, filteredStudies.size());
    }

    private List<GetAllStudiesResponse> paginateAndConvertToResponse(PageRequest pageRequest, List<Study> filteredStudies) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), filteredStudies.size());

        List<Study> pageContent = filteredStudies.subList(start, end);

        return pageContent.stream()
            .map(studyMapper::toGetAllStudyResponse)
            .collect(Collectors.toList());
    }

    private List<Study> filterStudies(StudyType studyType,
                                      Set<StudyCategory> studyCategories,
                                      Set<StudyLevel> studyLevels,
                                      StudyFilter studyFilters,
                                      Double maxPrice,
                                      Double minPrice,
                                      List<Study> studies) {

        log.info("Applying filters: studyTypes={}, studyFilters={}, studyCategories={}, studyLevels={}, maxPrice={}, minPrice={}",
            studyType, studyFilters, studyCategories, studyLevels, maxPrice, minPrice);

        if (studyType == null && studyFilters == null && studyCategories == null && studyLevels == null && maxPrice == null && minPrice == null) {
            log.info("No filter applied, returning all studies");
            return studies;
        }

        List<Study> filteredStudy = new ArrayList<>();

        filteredStudy = applyFilterByType(studyType, studies, filteredStudy);
        filteredStudy = applyFilterByCategory(studyCategories, studies, filteredStudy);
        filteredStudy = applyFilterByLevel(studyLevels, studies, filteredStudy);
        filteredStudy = applyFilterByEnum(studies, filteredStudy, studyFilters);
        filteredStudy = applyFilterByRangePrice(filteredStudy, minPrice, maxPrice);

        log.info("filtered study count: {}", filteredStudy.size());

        return filteredStudy;
    }

    private List<Study> applyFilterByLevel(Set<StudyLevel> levels,
                                           List<Study> studies,
                                           List<Study> filteredStudy) {
        if (levels == null || levels.isEmpty() ) {
            log.info("No level filter applied");
            return filteredStudy;
        }

        log.info("Applying level filter: {}", levels);
        List<Study> studyList = filteredStudy.isEmpty() ? studies : filteredStudy;
        return studyList.stream()
            .filter(study -> levels.contains(study.getLevel()))
            .collect(Collectors.toList());
    }

    private List<Study> applyFilterByCategory(Set<StudyCategory> categories,
                                              List<Study> studies,
                                              List<Study> filteredStudy) {
        if (categories == null || categories.isEmpty()) {
            log.info("No category filter applied");
            return filteredStudy;
        }

        log.info("Applying categories filter: {}", categories);
        List<Study> studyList = filteredStudy.isEmpty() ? studies : filteredStudy;
        return studyList.stream()
            .filter(category -> categories.contains(category.getCategory()))
            .collect(Collectors.toList());
    }

    private List<Study> applyFilterByType(StudyType types,
                                          List<Study> studies,
                                          List<Study> filteredStudy) {
        if (types == null) {
            log.info("No type filter applied");
            return filteredStudy;
        }

        log.info("Applying types filter: {}", types);
        List<Study> studyList = filteredStudy.isEmpty() ? studies : filteredStudy;
        return studyList.stream()
            .filter(study -> types.equals(study.getType()))
            .collect(Collectors.toList());
    }

    private List<Study> applyFilterByEnum(List<Study> studies,
                                          List<Study> filteredStudy,
                                          StudyFilter studyFilters) {
        if (studyFilters == null) {
            log.info("No enum filter applied");
            return filteredStudy;
        }

        switch (studyFilters) {
            case POPULAR:
                return applyFilterByPopular(studies, filteredStudy);
            case RECENTLY_ADDED :
                return applyFilterByRecentlyAdded(studies, filteredStudy);
            case DISCOUNT:
                return applyFilterByDiscount(studies, filteredStudy);
            default:
                return filteredStudy;
        }
    }

    private List<Study> applyFilterByDiscount(List<Study> studies, List<Study> filteredStudy) {
        List<Study> studyList = filteredStudy.isEmpty() ? studies : filteredStudy;

        log.info("Applying discount filter");
        return studyList.stream()
            .filter(study -> study.getDiscount() != null && study.getDiscount() > 0)
            .collect(Collectors.toList());
    }

    private List<Study> applyFilterByPopular(List<Study> studies,
                                             List<Study> filteredStudy) {
        List<Study> studyList = filteredStudy.isEmpty() ? studies : filteredStudy;

        log.info("Applying popular filter");
        return studyList.stream()
            .filter(study -> !study.getUsers().isEmpty())
            .sorted((study1, study2) -> Integer.compare(study2.getUsers().size(), study1.getUsers().size()))
            .collect(Collectors.toList());
    }

    private List<Study> applyFilterByRecentlyAdded(List<Study> studies,
                                                   List<Study> filteredStudy) {
        List<Study> studyList = filteredStudy.isEmpty() ? studies : filteredStudy;

        log.info("Applying recently added filter");
        return studyList.stream()
            .sorted(Comparator.comparing((Study::getCreatedAt)).reversed())
            .collect(Collectors.toList());
    }

    private List<Study> applyFilterByRangePrice(List<Study> studies,
                                                Double minPrice,
                                                Double maxPrice) {
        if (minPrice == null && maxPrice == null) {
            log.info("No price range filter applied");
            return studies;
        }

        log.info("Applying price range filter, maxPrice: {} and minPrice: {}", maxPrice, minPrice);
        return studies.stream()
            .filter(study -> {
                Integer price = study.getPrice();
                boolean min = (minPrice == null) || price >= minPrice;
                boolean max = (maxPrice == null) || price <= maxPrice;
                return min && max;
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
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
    @Transactional
    public UpdateStudyResponse updateStudy(String code, UpdateStudyRequest request) {
        Study study = studyRepository.findByCode(code)
            .orElseThrow(() -> {
                log.warn("Study with code : {}, not found!", code);
                return new ResponseStatusException(HttpStatus.NOT_FOUND,"Study not found!");
            });

        updateStudyProperties(study, request);

        studyRepository.save(study);
        log.info("Study successfully updated!");

        return studyMapper.toUpdateStudyResponse(study);
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
