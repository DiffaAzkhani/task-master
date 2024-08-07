package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyFilter;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.model.request.CreateNewStudyRequest;
import com.taskmaster.taskmaster.model.request.UpdateStudyRequest;
import com.taskmaster.taskmaster.model.response.CreateNewStudyResponse;
import com.taskmaster.taskmaster.model.response.GetAllStudiesResponse;
import com.taskmaster.taskmaster.model.response.GetStudyByCodeResponse;
import com.taskmaster.taskmaster.model.response.UpdateStudyResponse;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface StudyService {

    CreateNewStudyResponse createNewStudy(CreateNewStudyRequest request);

    GetStudyByCodeResponse getStudyByCode(String code);

    Page<GetAllStudiesResponse> getAllStudies(StudyType studyType,
                                              Set<StudyCategory> studyCategories,
                                              Set<StudyLevel> studyLevels,
                                              StudyFilter studyFilters,
                                              Double minPrice,
                                              Double maxPrice,
                                              int page,
                                              int size);

    void deleteStudy(String code);

    UpdateStudyResponse updateStudy(String code, UpdateStudyRequest request);

}
