package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyFilter;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.model.request.CreateNewStudyRequest;
import com.taskmaster.taskmaster.model.request.UpdateStudyRequest;
import com.taskmaster.taskmaster.model.response.CreateNewStudyResponse;
import com.taskmaster.taskmaster.model.response.GetStudiesResponse;
import com.taskmaster.taskmaster.model.response.GetStudyByIdResponse;
import com.taskmaster.taskmaster.model.response.UpdateStudyResponse;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface StudyService {

    CreateNewStudyResponse createNewStudy(CreateNewStudyRequest request);

    GetStudyByIdResponse getStudyById(Long studyId);

    Page<GetStudiesResponse> getAllStudies(StudyType studyType,
                                           Set<StudyCategory> studyCategories,
                                           Set<StudyLevel> studyLevels,
                                           StudyFilter studyFilters,
                                           Integer minPrice,
                                           Integer maxPrice,
                                           int page,
                                           int size);

    void deleteStudy(Long studyID);

    UpdateStudyResponse updateStudy(Long studyId, UpdateStudyRequest request);

    Page<GetStudiesResponse> getMyStudies(int page, int size);

}
