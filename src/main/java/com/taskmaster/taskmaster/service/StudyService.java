package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddStudyRequest;
import com.taskmaster.taskmaster.model.response.StudyResponse;

public interface StudyService {
    StudyResponse addStudy(AddStudyRequest addStudyRequest);
}
