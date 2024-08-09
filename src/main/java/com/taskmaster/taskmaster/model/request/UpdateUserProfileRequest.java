package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserProfileRequest {

    @Email(message = "Email is invalid")
    @Size(max = 100, message = "Email should not be more than 100 characters")
    private String email;

    @Size(max = 25, message = "First name should not be more than 25 characters")
    private String firstName;

    @Size(max = 25, message = "Last name should not be more than 25 characters")
    private String lastName;

    @Size(max = 15, message = "Phone number should not be more than 15 characters")
    private String phone;

    private String profileImage;

}
