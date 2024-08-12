package com.taskmaster.taskmaster.model.response;

import com.taskmaster.taskmaster.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllUsersResponse {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private List<UserRole> userRole;

    private String createdAt;

    private String updatedAt;

}
