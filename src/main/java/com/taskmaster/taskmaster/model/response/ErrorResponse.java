package com.taskmaster.taskmaster.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String message;

    private List<FieldErrorDetailResponse> details;

}
