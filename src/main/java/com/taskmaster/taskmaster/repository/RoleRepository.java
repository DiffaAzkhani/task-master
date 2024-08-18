package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Role;
import com.taskmaster.taskmaster.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(UserRole userRole);

}
