package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Answer;
import com.taskmaster.taskmaster.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByIdAndQuestion(Long answerId, Question question);

    Optional<Answer> findByQuestionAndIsCorrectTrue(Question question);

}
