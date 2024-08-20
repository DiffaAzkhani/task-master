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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class StudyRepositoryTest {

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    private Set<Study> testStudy;

    private User testUser;

    @BeforeEach
    void setUp() {
        testStudy = new HashSet<>();

        testStudy.add(Study.builder()
            .code("MATH.12.12345")
            .name("Test Study Mathematics")
            .price(100000)
            .discount(0)
            .link("test-link-math.com")
            .category(StudyCategory.MATHEMATICS)
            .type(StudyType.PREMIUM)
            .level(StudyLevel.GRADE_12)
            .build());

        testStudy.add(Study.builder()
            .code("PHYS.10.12345")
            .name("Test Study Physics")
            .price(50000)
            .discount(0)
            .link("test-link-phyiscs.com")
            .category(StudyCategory.PHYSICS)
            .type(StudyType.PREMIUM)
            .level(StudyLevel.GRADE_10)
            .build());

        testStudy.add(Study.builder()
            .code("CHEM.11.12345")
            .name("Test Study Chemistry")
            .price(0)
            .discount(0)
            .link("test-link-chemistry.com")
            .category(StudyCategory.CHEMISTRY)
            .type(StudyType.FREE)
            .level(StudyLevel.GRADE_11)
            .build());

        testStudy.add(Study.builder()
            .code("INDO.11.12345")
            .name("Test Study Indonesian")
            .price(5000)
            .discount(50)
            .link("test-link-indonesian.com")
            .category(StudyCategory.INDONESIAN)
            .type(StudyType.PREMIUM)
            .level(StudyLevel.GRADE_11)
            .build());

        testUser = User.builder()
            .username("TestUser")
            .password("testuser123")
            .firstName("test")
            .lastName("user")
            .email("test.email@example.com")
            .phone("12345")
            .studies(testStudy)
            .build();

        userRepository.save(testUser);
        studyRepository.saveAll(testStudy);
    }

    @AfterEach
    void tearDown() {
        studyRepository.deleteAll(testStudy);
        userRepository.delete(testUser);
    }

    @Test
    void givenStudyCode_whenCheckingStudyByCode_thenReturnTrueResponse() {
        boolean existsStudy = studyRepository.existsByCode("MATH.12.12345");

        assertTrue(existsStudy);
    }

    @Test
    void givenStudyCode_whenCheckingStudyByCode_thenReturnFalseResponse() {
        boolean existsStudy = studyRepository.existsByCode("WRONG.CODE");

        assertFalse(existsStudy);
    }

    @Test
    void givenStudyNameAndLink_whenCheckingStudyByNameAndLink_thenReturnTrueResponse() {
        boolean existsStudy = studyRepository.existsByNameOrLink("Test Study Physics", "test-link-physics.com");

        assertTrue(existsStudy);
    }

    @Test
    void givenStudyNameAndLink_whenCheckingStudyByNameAndLink_thenReturnFalseResponse() {
        boolean existsStudy = studyRepository.existsByNameOrLink("Wrong Study", "worng-test-link.com");

        assertFalse(existsStudy);
    }

    @Test
    void givenStudyCode_whenFindStudyByCode_thenStudyIsFound() {
        Study foundStudy = studyRepository.findByCode("INDO.11.12345")
            .orElse(null);

        assertNotNull(foundStudy);
        boolean existingStudy = testStudy.stream()
            .anyMatch(study -> study.getCode().equals(foundStudy.getCode()) &&
                study.getName().equals(foundStudy.getName()));

        assertTrue(existingStudy);
    }

    @Test
    void givenStudyCode_whenFindStudyByCode_thenStudyIsNotFound() {
        Study foundStudy = studyRepository.findByCode("WRONG.CODE")
            .orElse(null);

        assertNull(foundStudy);
    }

    @Test
    void givenUser_whenCheckingStudyByUsers_thenReturnTrueResponse() {
        boolean existsStudy = studyRepository.existsByUsers(testUser);

        assertTrue(existsStudy);
    }

    @Test
    void givenUser_whenCheckingStudyByUsers_thenReturnFalseResponse() {
        boolean existsStudy = studyRepository.existsByUsers(null);

        assertFalse(existsStudy);
    }

    @Test
    void givenUserUsername_whenFindStudyByUserUsername_thenStudyIsFoundWithPaging() {
        Pageable studyPage = PageRequest.of(0,2);
        Page<Study> foundStudy = studyRepository.findByUsers_Username(testUser.getUsername(), studyPage);

        assertFalse(foundStudy.getContent().isEmpty());
        assertEquals(2, foundStudy.getContent().size());
        assertEquals(2, foundStudy.getTotalPages());
        assertEquals(4, foundStudy.getTotalElements());

        boolean existingStudy = testStudy.stream()
                .anyMatch(study -> "CHEM.11.12345".equals(study.getCode()));

        assertTrue(existingStudy);
    }

    @Test
    void givenUserUsername_whenFindStudyByUserUsername_thenStudyIsNotFoundWithPaging() {
        Pageable studyPage = PageRequest.of(0,2);
        Page<Study> foundStudy = studyRepository.findByUsers_Username("wrongusername", studyPage);

        assertTrue(foundStudy.getContent().isEmpty());
        assertEquals(0, foundStudy.getContent().size());
        assertEquals(0, foundStudy.getTotalPages());
        assertEquals(0, foundStudy.getTotalElements());
    }

    @Test
    void givenFilterParameter_whenFindStudyByFiltersParameter_thenStudyIsFoundWithPaging() {
        Set<StudyCategory> studyCategoriesPhysics = EnumSet.of(StudyCategory.PHYSICS);
        Set<StudyLevel> studyLevelsGrade10 = EnumSet.of(StudyLevel.GRADE_10);

        Pageable studyPage = PageRequest.of(0,3);
        Page<Study> foundStudyByAllFilterThenFound = studyRepository.findByFilters(
            StudyType.PREMIUM,
            studyCategoriesPhysics,
            studyLevelsGrade10,
            25000,
            200000,
            studyPage);

        // 1 (PHYS.10.12345)
        assertFalse(foundStudyByAllFilterThenFound.getContent().isEmpty());
        assertEquals(1, foundStudyByAllFilterThenFound.getContent().size());
    }

    @Test
    void givenFilterMinPriceAndMaxPrice_whenFindStudyByFilter_thenStudyIsFound() {
        Pageable studyPage = PageRequest.of(0,3);
        Page<Study> foundStudyMinPriceAndMaxPrice = studyRepository.findByFilters(
            null,
            null,
            null,
            5000,
            80000,
            studyPage);

        // found 2 (INDO.11.12345, PHYS.10.12345)
        assertFalse(foundStudyMinPriceAndMaxPrice.getContent().isEmpty());
        assertEquals(2, foundStudyMinPriceAndMaxPrice.getContent().size());
    }

    @Test
    void givenFilterStudyType_whenFindStudyByFilter_thenStudyIsFound() {
        Pageable studyPage = PageRequest.of(0,3);
        Page<Study> foundStudyByTypeFreeAndPrice = studyRepository.findByFilters(
            StudyType.FREE,
            null,
            null,
            null,
            null,
            studyPage);

        // found 1 (CHEM.11.12345)
        assertFalse(foundStudyByTypeFreeAndPrice.getContent().isEmpty());
        assertEquals(1, foundStudyByTypeFreeAndPrice.getContent().size());
    }

    @Test
    void givenFilterStudyCategories_whenFindStudyByFilter_thenStudyIsFound() {
        Set<StudyCategory> studyCategoriesPhysics = EnumSet.of(StudyCategory.PHYSICS);

        Pageable studyPage = PageRequest.of(0,3);
        Page<Study> foundStudyByCategoriesPhysics = studyRepository.findByFilters(
            null,
            studyCategoriesPhysics,
            null,
            null,
            null,
            studyPage);

        // found 1 (PHYS.10.12345)
        assertFalse(foundStudyByCategoriesPhysics.getContent().isEmpty());
        assertEquals(1, foundStudyByCategoriesPhysics.getContent().size());
    }

    @Test
    void givenFilterStudyLevelsGrade11_whenFindStudyByFilter_thenStudyIsFound() {
        Set<StudyLevel> studyLevelsGrade11 = EnumSet.of(StudyLevel.GRADE_11);

        Pageable studyPage = PageRequest.of(0,3);
        Page<Study> foundStudyByLevelsGrade_11 = studyRepository.findByFilters(
            null,
            null,
            studyLevelsGrade11,
            null,
            null,
            studyPage);

        // found 2 (CHEM.11.12345, INDO.11.12345)
        assertFalse(foundStudyByLevelsGrade_11.getContent().isEmpty());
        assertEquals(2, foundStudyByLevelsGrade_11.getContent().size());
    }

    @Test
    void givenFilterAllParameters_whenFindStudyByFilter_thenStudyIsNotFound() {
        Set<StudyCategory> studyCategoriesBiology = EnumSet.of(StudyCategory.BIOLOGY);
        Set<StudyLevel> studyLevelsGrade10 = EnumSet.of(StudyLevel.GRADE_10);

        Pageable studyPage = PageRequest.of(0,3);
        Page<Study> foundStudyByAllFilterThenNotFound = studyRepository.findByFilters(
            StudyType.FREE,
            studyCategoriesBiology,
            studyLevelsGrade10,
            75000,
            250000,
            studyPage);

        // didn't find any study
        assertTrue(foundStudyByAllFilterThenNotFound.getContent().isEmpty());
        assertEquals(0, foundStudyByAllFilterThenNotFound.getContent().size());
    }

}
