package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Answer;
import com.taskmaster.taskmaster.entity.Question;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudyRepository studyRepository;

    private List<Answer> testAnswer;

    private Question testQuestion;

    private Study testStudy;

    @BeforeEach
    void setUp() {
         testStudy = Study.builder()
            .code("MATH.11.11111")
            .name("Test Study")
            .price(150000)
            .discount(0)
            .link("test-study.mp4")
            .type(StudyType.PREMIUM)
            .level(StudyLevel.GRADE_12)
            .category(StudyCategory.MATHEMATICS)
            .build();

        studyRepository.save(testStudy);

        testQuestion = Question.builder()
            .questionText("Berapa 2 + 3 ?")
            .imageUrl("math.jpg")
            .explanation("karena hasilnya 5")
            .study(testStudy)
            .build();

        questionRepository.save(testQuestion);

        testAnswer = new ArrayList<>();

        testAnswer.add(Answer.builder()
            .answerText("5")
            .isCorrect(true)
            .question(testQuestion)
            .build());

        testAnswer.add(Answer.builder()
            .answerText("1")
            .isCorrect(false)
            .question(testQuestion)
            .build());

        testQuestion.setAnswers(testAnswer);
        questionRepository.save(testQuestion);
    }

    @AfterEach
    void tearDown() {
        answerRepository.deleteAll(testAnswer);
        questionRepository.delete(testQuestion);
        studyRepository.delete(testStudy);
    }

    @Test
    void givenIdAndQuestion_whenFindAnswerByIdAndQuestion_thenAnswerIsFound() {
        Answer foundAnswer = answerRepository.findByIdAndQuestion(testAnswer.get(0).getId(), testQuestion)
            .orElse(null);

        assertNotNull(foundAnswer);
        assertEquals(testAnswer.get(0), foundAnswer);
    }

    @Test
    void givenIdAndQuestion_whenFindAnswerByIdAndQuestion_thenAnswerIsNotFound() {
        Answer foundAnswer = answerRepository.findByIdAndQuestion(5L, testQuestion)
            .orElse(null);

        assertNull(foundAnswer);
    }

    @Test
    void givenQuestion_whenFindAnswerByQuestionAndIsCorrectTrue_thenAnswerIsFound() {
        Answer foundAnswer = answerRepository.findByQuestionAndIsCorrectTrue(testQuestion)
            .orElse(null);

        assertNotNull(foundAnswer);
        assertEquals(testAnswer.get(0) ,foundAnswer);
        assertNotEquals(testAnswer.get(1) ,foundAnswer);
    }

    @Test
    void givenQuestion_whenFindAnswerByQuestionAndIsCorrectTrue_thenAnswerIsNotFound() {
        Answer foundAnswer = answerRepository.findByQuestionAndIsCorrectTrue(null)
            .orElse(null);

        assertNull(foundAnswer);
    }

}
