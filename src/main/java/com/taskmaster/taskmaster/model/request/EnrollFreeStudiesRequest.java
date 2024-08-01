package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollFreeStudiesRequest {

    @NotBlank(message = "Username is required")
    @Size(max = 100, message = "Username should not be more than 100 characters")
    String username;

    @NotNull(message = "Free items detail list cannot be null")
    @NotEmpty(message = "Free items detail list cannot be empty")
    @Valid
    List<FreeItemsDetailRequest> freeItemsDetailRequestList;

}
