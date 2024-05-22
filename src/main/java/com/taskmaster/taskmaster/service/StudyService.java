package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddStudyRequest;
import com.taskmaster.taskmaster.model.request.UpdateStudyRequest;
import com.taskmaster.taskmaster.model.response.StudyResponse;
import org.springframework.data.domain.Page;

public interface StudyService {
    StudyResponse addStudy(AddStudyRequest request);

    StudyResponse getStudy(String code);

    Page<StudyResponse> getAllStudyMaterial(int page, int size);

    void deleteStudy(String code);

    StudyResponse updateStudy(String code, UpdateStudyRequest request);

}
