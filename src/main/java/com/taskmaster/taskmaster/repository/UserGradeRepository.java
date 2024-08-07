package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.entity.UserGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGradeRepository extends JpaRepository<UserGrade, Long> {

    UserGrade findByUserAndStudy(User user, Study study);

}
