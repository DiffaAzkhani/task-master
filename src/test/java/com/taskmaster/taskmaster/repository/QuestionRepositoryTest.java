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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired StudyRepository studyRepository;

    private Study testStudy;

    private List<Question> testQuestionList;

    @BeforeEach
    public void setUp() {
        testQuestionList = new ArrayList<>();

        testStudy = Study.builder()
            .code("BLGY.12.12345")
            .name("Test Study")
            .price(150000)
            .discount(0)
            .link("test-study.mp4")
            .type(StudyType.PREMIUM)
            .level(StudyLevel.GRADE_12)
            .category(StudyCategory.BIOLOGY)
            .build();

        studyRepository.save(testStudy);

        Question firstQuestion = Question.builder()
            .questionText("Dimana habitat asli komodo ?")
            .explanation("Habitat asli komodo adalah di beberapa pulau di Indonesia, terutama di Pulau Komodo, Rinca, Flores, Gili Motang, dan Gili Dasami. Pulau-pulau ini terletak di wilayah Nusa Tenggara Timur.")
            .imageUrl("komodo1.jpg")
            .study(testStudy)
            .build();

        List<Answer> firstQuestionAnswers = new ArrayList<>();

        firstQuestionAnswers.add(Answer.builder()
            .answerText("Wilayah Nusa Tenggara Timur : Pulau Komodo, Rinca, Flores, Gili Motang")
            .isCorrect(true)
            .question(firstQuestion)
            .build());

        firstQuestionAnswers.add(Answer.builder()
            .answerText("Wilayah Jakarta : Jakut, Jabar, Jaksel")
            .isCorrect(false)
            .question(firstQuestion)
            .build());

        firstQuestion.setAnswers(firstQuestionAnswers);
        questionRepository.save(firstQuestion);

        Question secondQuestion = Question.builder()
            .questionText("Apa nama ilmiah dari komodo ?")
            .explanation("Nama ilmiah dari komodo adalah Varanus komodoensis")
            .imageUrl("habitat-komodo.jpg")
            .study(testStudy)
            .build();

        List<Answer> secondQuestionAnswers = new ArrayList<>();

        secondQuestionAnswers.add(Answer.builder()
            .answerText("Varanus komodoensis")
            .isCorrect(true)
            .question(secondQuestion)
            .build());

        secondQuestionAnswers.add(Answer.builder()
            .answerText("Oryza Sativa")
            .isCorrect(false)
            .question(secondQuestion)
            .build());

        secondQuestion.setAnswers(secondQuestionAnswers);
        questionRepository.save(secondQuestion);

        testQuestionList.add(firstQuestion);
        testQuestionList.add(secondQuestion);
    }

    @AfterEach
    public void tearDown() {
        questionRepository.deleteAll();
        studyRepository.delete(testStudy);
    }

    @Test
    public void givenStudyId_whenFindQuestionsByStudyId_thenQuestionsIsFound() {
        List<Question> foundQuestion = questionRepository.findByStudyId(testStudy.getId());

        assertFalse(foundQuestion.isEmpty());
    }

    @Test
    public void givenStudyId_whenFindQuestionsByStudyId_thenQuestionsNotFound() {
        List<Question> foundQuestion = questionRepository.findByStudyId(5L);

        assertTrue(foundQuestion.isEmpty());
    }

    @Test
    public void givenQuestionIdAndStudy_whenFindQuestionsByIdAndStudy_thenQuestionsIsFound() {
        List<Question> testQuestionList = questionRepository.findAll();

        Question foundQuestion = questionRepository.findByIdAndStudy(testQuestionList.get(0).getId(), testStudy)
            .orElse(null);

        assertNotNull(foundQuestion);
        assertEquals(testQuestionList.get(0).getQuestionText(), foundQuestion.getQuestionText());
        assertEquals(testStudy, foundQuestion.getStudy());
        assertEquals(testQuestionList.get(0).getAnswers(), foundQuestion.getAnswers());
    }

    @Test
    public void givenQuestionIdAndStudy_whenFindQuestionsByIdAndStudy_thenQuestionsNotFound() {
        Question foundQuestion = questionRepository.findByIdAndStudy(9L, testStudy)
            .orElse(null);

        assertNull(foundQuestion);
    }

    @Test
    public void givenStudy_whenFindQuestions_thenQuestionsIsFound() {
        List<Question> foundQuestion = questionRepository.findByStudy(testStudy);

        assertFalse(foundQuestion.isEmpty());
        assertEquals(testQuestionList, foundQuestion);
    }

    @Test
    public void givenStudy_whenFindQuestions_thenQuestionsNotFound() {
        List<Question> foundQuestion = questionRepository.findByStudy(null);

        assertTrue(foundQuestion.isEmpty());
    }

    @Test
    public void givenStudy_whenFindQuestions_thenQuestionsIsFoundWithPagination() {
        Pageable questionPage = PageRequest.of(0,2);
        Page<Question> foundQuestion = questionRepository.findByStudy(testStudy, questionPage);

        assertEquals(2, foundQuestion.getContent().size());
        assertEquals(1, foundQuestion.getTotalPages());
        assertEquals(2, foundQuestion.getTotalElements());
        assertEquals(testQuestionList, foundQuestion.getContent());
    }

    @Test
    public void givenStudy_whenFindQuestions_thenQuestionsNotFoundWithPagination() {
        Pageable questionPage = PageRequest.of(0,2);
        Page<Question> foundQuestion = questionRepository.findByStudy(null, questionPage);

        assertTrue(foundQuestion.getContent().isEmpty());
    }

}
