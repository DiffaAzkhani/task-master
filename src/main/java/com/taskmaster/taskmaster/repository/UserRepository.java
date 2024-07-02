package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameAndEmail(String username, String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

}
