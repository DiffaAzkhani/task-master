package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    private User testUser;

    private Study testStudy;

    @BeforeEach
    void setUp() {
        Set<Study> studySet = new HashSet<>();

        testStudy = Study.builder()
            .code("MATH.12.12345")
            .name("Test Study")
            .price(100000)
            .discount(0)
            .link("test-link.com")
            .category(StudyCategory.MATHEMATICS)
            .type(StudyType.PREMIUM)
            .level(StudyLevel.GRADE_12)
            .build();

        studySet.add(testStudy);

        testUser = User.builder()
            .username("TestUser")
            .password("testuser123")
            .firstName("test")
            .lastName("user")
            .email("test.email@example.com")
            .phone("12345")
            .studies(studySet)
            .build();

        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.delete(testUser);
        studyRepository.delete(testStudy);
    }

    @Test
    void givenUser_whenCheckingUsernameAndEmail_thenReturnTrueResponse() {
        boolean existsUser = userRepository.existsByUsernameAndEmail(testUser.getUsername(), testUser.getEmail());
        assertTrue(existsUser);
    }

    @Test
    void givenUser_whenCheckingUsernameAndEmail_thenReturnFalseResponse() {
        boolean existsUser = userRepository.existsByUsernameAndEmail("Wrong Username", "wrong.email@example.com");
        assertFalse(existsUser);
    }

    @Test
    void givenUser_whenFindByUsernameOrEmail_thenUserIsFound() {
        User foundUser = userRepository.findByUsernameOrEmail(testUser.getUsername(), testUser.getEmail())
            .orElse(null);

        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    void givenUser_whenFindByUsernameOrEmail_thenUserIsNotFound() {
        User foundUser = userRepository.findByUsernameOrEmail("Wrong Username", "wrong.email@example.com")
            .orElse(null);

        assertNull(foundUser);
    }

    @Test
    void givenUser_whenFindByUsername_thenUserIsFound() {
        User foundUser = userRepository.findByUsername(testUser.getUsername())
            .orElse(null);

        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    void givenUser_whenFindByUsername_thenUserIsNotFound() {
        User foundUser = userRepository.findByUsername("Wrong Username")
            .orElse(null);

        assertNull(foundUser);
    }

    @Test
    void givenUser_whenCheckingEmail_thenReturnTrueResponse() {
        boolean existsUser = userRepository.existsByEmail(testUser.getEmail());
        assertTrue(existsUser);
    }

    @Test
    void givenUser_whenCheckingEmail_thenReturnFalseResponse() {
        boolean existsUser = userRepository.existsByEmail("wrong.email@example.com");
        assertFalse(existsUser);
    }

    @Test
    void givenUser_whenFindByEmail_thenUserIsFound() {
        User foundUser = userRepository.findByEmail(testUser.getEmail())
            .orElse(null);

        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    void givenUser_whenFindByEmail_thenUserIsNotFound() {
        User foundUser = userRepository.findByEmail("wrong.username@example.com")
            .orElse(null);

        assertNull(foundUser);
    }

    @Test
    void givenUsernameAndStudies_whenCheckingUsernameAndStudies_thenReturnTrueResponse() {
        boolean existsUser = userRepository.existsByUsernameAndStudies(testUser.getUsername(), testStudy);
        assertTrue(existsUser);
    }

    @Test
    void givenUsernameAndStudies_whenCheckingUsernameAndStudies_thenReturnFalseResponse() {
        boolean existsUser = userRepository.existsByUsernameAndStudies("Wrong username" ,testStudy);
        assertFalse(existsUser);
    }

    @Test
    void givenUserPhone_whenCheckingPhone_thenReturnTrueResponse() {
        boolean existsUser = userRepository.existsByPhone(testUser.getPhone());
        assertTrue(existsUser);
    }

    @Test
    void givenUserPhone_whenCheckingPhone_thenReturnFalseResponse() {
        boolean existsUser = userRepository.existsByEmail("54321");
        assertFalse(existsUser);
    }

}
