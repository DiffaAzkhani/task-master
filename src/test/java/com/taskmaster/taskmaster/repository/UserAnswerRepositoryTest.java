package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Answer;
import com.taskmaster.taskmaster.entity.Question;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.entity.UserAnswer;
import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserAnswerRepositoryTest {

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private List<UserAnswer> testUserAnswer;

    private User testUser;

    private Study testStudy;

    private List<Question> testQuestion;

    @BeforeEach
    void setUp() {
        testStudy = Study.builder()
            .code("MATH.12.12345")
            .name("Test Study Mathematics")
            .price(100000)
            .discount(0)
            .link("test-link-math.com")
            .category(StudyCategory.MATHEMATICS)
            .type(StudyType.PREMIUM)
            .level(StudyLevel.GRADE_12)
            .build();

        studyRepository.save(testStudy);
        Set<Study> userStudies = new HashSet<>();
        userStudies.add(testStudy);

        testUser = User.builder()
            .username("TestUser")
            .password("testuser123")
            .firstName("test")
            .lastName("user")
            .email("test.email@example.com")
            .phone("12345")
            .studies(userStudies)
            .build();

        userRepository.save(testUser);

        testQuestion = new ArrayList<>();

        Question firstQuestion = Question.builder()
            .questionText("Dimana habitat asli komodo ?")
            .explanation("Habitat asli komodo adalah di beberapa pulau di Indonesia, terutama di Pulau Komodo, Rinca, Flores, Gili Motang, dan Gili Dasami. Pulau-pulau ini terletak di wilayah Nusa Tenggara Timur.")
            .imageUrl("komodo1.jpg")
            .study(testStudy)
            .build();

        List<Answer> testAnswerQuestion1 = new ArrayList<>();

        testAnswerQuestion1.add(Answer.builder()
            .answerText("Wilayah Nusa Tenggara Timur : Pulau Komodo, Rinca, Flores, Gili Motang")
            .isCorrect(true)
            .question(firstQuestion)
            .build());

        testAnswerQuestion1.add(Answer.builder()
            .answerText("Wilayah Jakarta : Jakut, Jabar, Jaksel")
            .isCorrect(false)
            .question(firstQuestion)
            .build());

        firstQuestion.setAnswers(testAnswerQuestion1);
        questionRepository.save(firstQuestion);

        Question secondQuestion = Question.builder()
            .questionText("Apa nama ilmiah dari komodo ?")
            .explanation("Nama ilmiah dari komodo adalah Varanus komodoensis")
            .imageUrl("habitat-komodo.jpg")
            .study(testStudy)
            .build();

        List<Answer> testAnswerQuestion2 = new ArrayList<>();

        testAnswerQuestion2.add(Answer.builder()
            .answerText("Varanus komodoensis")
            .isCorrect(true)
            .question(secondQuestion)
            .build());

        testAnswerQuestion2.add(Answer.builder()
            .answerText("Oryza Sativa")
            .isCorrect(false)
            .question(secondQuestion)
            .build());

        secondQuestion.setAnswers(testAnswerQuestion2);
        questionRepository.save(secondQuestion);

        testQuestion.add(firstQuestion);
        testQuestion.add(secondQuestion);

        testUserAnswer = new ArrayList<>();

        testUserAnswer.add(UserAnswer.builder()
            .user(testUser)
            .study(testStudy)
            .question(testQuestion.get(0))
            .answer(testAnswerQuestion1.get(0))
            .build());

        testUserAnswer.add(UserAnswer.builder()
            .user(testUser)
            .study(testStudy)
            .question(testQuestion.get(1))
            .answer(testAnswerQuestion2.get(0))
            .build());

        userAnswerRepository.saveAll(testUserAnswer);
    }

    @AfterEach
    void tearDown() {
        userRepository.delete(testUser);
        studyRepository.delete(testStudy);
    }

    @Test
    void givenUserAndQuestion_whenCheckingUserAnswers_thenReturnTrueResponse() {
        boolean existsUserAnswers = userAnswerRepository.existsByUserAndQuestion(testUser, testQuestion.get(0));

        assertTrue(existsUserAnswers);
    }

    @Test
    void givenUserAndQuestion_whenCheckingUserAnswers_thenReturnFalseResponse() {
        boolean existsUserAnswers = userAnswerRepository.existsByUserAndQuestion(null, testQuestion.get(0));

        assertFalse(existsUserAnswers);
    }

    @Test
    void givenUserAndQuestion_whenFindUserAnswerByUserAndListQuestion_thenUserAnswersIsFound() {
        List<UserAnswer> foundUserAnswers = userAnswerRepository.findByUserAndQuestionIn(testUser, testQuestion);

        assertFalse(foundUserAnswers.isEmpty());
        assertEquals(testUserAnswer, foundUserAnswers);
        assertEquals(testUserAnswer.get(0).getQuestion(), foundUserAnswers.get(0).getQuestion());
        assertEquals(testUserAnswer.get(1).getQuestion(), foundUserAnswers.get(1).getQuestion());
    }

    @Test
    void givenUserAndQuestion_whenFindUserAnswerByUserAndListQuestion_thenUserAnswersIsNotFound() {
        List<UserAnswer> foundUserAnswers = userAnswerRepository.findByUserAndQuestionIn(null, testQuestion);

        assertTrue(foundUserAnswers.isEmpty());
        assertNotEquals(testUserAnswer, foundUserAnswers);
    }

    @Test
    void givenUserUsernameAndStudy_whenFindUserAnswerByUserUsernameAndStudy_thenUserAnswersIsFound() {
        List<UserAnswer> foundUserAnswers = userAnswerRepository.findByUser_UsernameAndStudy(testUser.getUsername(), testStudy);

        assertFalse(foundUserAnswers.isEmpty());
        assertEquals(testUserAnswer, foundUserAnswers);
        assertEquals(testUserAnswer.get(0).getQuestion(), foundUserAnswers.get(0).getQuestion());
        assertEquals(testUserAnswer.get(1).getQuestion(), foundUserAnswers.get(1).getQuestion());
    }

    @Test
    void givenUserUsernameAndStudy_whenFindUserAnswerByUserUsernameAndStudy_thenUserAnswersIsNotFound() {
        List<UserAnswer> foundUserAnswers = userAnswerRepository.findByUser_UsernameAndStudy(testUser.getUsername(), null);

        assertTrue(foundUserAnswers.isEmpty());
        assertNotEquals(testUserAnswer, foundUserAnswers);
    }

}
