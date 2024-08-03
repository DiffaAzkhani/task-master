package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Question;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    boolean existsByUserAndQuestion(User user, Question question);

    List<UserAnswer> findByUserAndQuestionIn(User user, List<Question> questionList);

}
