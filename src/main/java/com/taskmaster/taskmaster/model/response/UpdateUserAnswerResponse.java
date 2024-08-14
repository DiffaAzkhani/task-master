package com.taskmaster.taskmaster.model.response;

import com.taskmaster.taskmaster.model.request.UpdateUserAnswerRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserAnswerResponse {

    private Long studyId;

    private List<UpdateUserAnswerRequest> userAnswerList;

}
