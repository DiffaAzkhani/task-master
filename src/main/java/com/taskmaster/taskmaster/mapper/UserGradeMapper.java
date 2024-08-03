package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.UserGrade;
import com.taskmaster.taskmaster.model.response.GradeSubmissionResponse;
import org.springframework.stereotype.Component;

@Component
public class UserGradeMapper {

    public GradeSubmissionResponse toUserGradeResponse(UserGrade userGrade) {
        return GradeSubmissionResponse.builder()
            .username(userGrade.getUser().getUsername())
            .studyCode(userGrade.getStudy().getCode())
            .score(userGrade.getScore())
            .gradedAt(TimeUtil.formatToString(userGrade.getGradedAt()))
            .build();
    }

}
