package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {

    Boolean existsByCode(String code);

    Boolean existsByNameOrLink(String name, String link);

    Optional<Study> findByCode(String code);

    boolean existsByUsers(User user);

}
