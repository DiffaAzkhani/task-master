package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Role;
import com.taskmaster.taskmaster.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(UserRole userRole);

}
