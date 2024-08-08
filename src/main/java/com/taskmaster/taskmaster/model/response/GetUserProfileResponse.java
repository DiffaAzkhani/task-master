package com.taskmaster.taskmaster.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserProfileResponse {

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String profileImage;

}
