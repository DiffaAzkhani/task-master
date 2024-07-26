package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Question;
import com.taskmaster.taskmaster.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByStudyId(Long studyId);

    Optional<Question> findByIdAndStudy(Long questionId, Study study);

}
