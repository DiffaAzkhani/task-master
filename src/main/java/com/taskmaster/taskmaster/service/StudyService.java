package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyFilter;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.model.request.AddStudyRequest;
import com.taskmaster.taskmaster.model.request.UpdateStudyRequest;
import com.taskmaster.taskmaster.model.response.AddStudyResponse;
import com.taskmaster.taskmaster.model.response.GetAllStudyResponse;
import com.taskmaster.taskmaster.model.response.GetStudyResponse;
import com.taskmaster.taskmaster.model.response.UpdateStudyResponse;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface StudyService {

    AddStudyResponse addStudy(AddStudyRequest request);

    GetStudyResponse getStudy(String code);

    Page<GetAllStudyResponse> getAllStudy(StudyType studyType,
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
