package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {

    Boolean existsByCode(String code);

    Boolean existsByNameOrLink(String name, String link);

}
