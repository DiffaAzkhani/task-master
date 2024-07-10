package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyFilter;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.model.request.AddStudyRequest;
import com.taskmaster.taskmaster.model.request.UpdateStudyRequest;
import com.taskmaster.taskmaster.model.response.StudyResponse;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface StudyService {
    StudyResponse addStudy(AddStudyRequest request);

    StudyResponse getStudy(String code);

    Page<StudyResponse> getAllStudy(StudyType studyType,
                                    Set<StudyCategory> studyCategories,
                                    Set<StudyLevel> studyLevels,
                                    StudyFilter studyFilters,
                                    Double minPrice,
                                    Double maxPrice,
                                    int page,
                                    int size);

    void deleteStudy(String code);

    StudyResponse updateStudy(String code, UpdateStudyRequest request);

}
