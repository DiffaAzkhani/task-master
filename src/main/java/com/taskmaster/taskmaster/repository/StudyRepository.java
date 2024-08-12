package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.enums.StudyCategory;
import com.taskmaster.taskmaster.enums.StudyLevel;
import com.taskmaster.taskmaster.enums.StudyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {

    Boolean existsByCode(String code);

    Boolean existsByNameOrLink(String name, String link);

    Optional<Study> findByCode(String code);

    boolean existsByUsers(User user);

    Page<Study> findByUsers_Username(String username, PageRequest pageRequest);

    @Query("SELECT s FROM Study s WHERE (:studyType IS NULL OR s.type = :studyType) " +
        "AND (:studyCategories IS NULL OR s.category IN :studyCategories) " +
        "AND (:studyLevels IS NULL OR s.level IN :studyLevels) " +
        "AND (:minPrice IS NULL OR s.price >= :minPrice) " +
        "AND (:maxPrice IS NULL OR s.price <= :maxPrice)")
    Page<Study> findByFilters(@Param("studyType") StudyType studyType,
                              @Param("studyCategories") Set<StudyCategory> studyCategories,
                              @Param("studyLevels") Set<StudyLevel> studyLevels,
                              @Param("minPrice") Integer minPrice,
                              @Param("maxPrice") Integer maxPrice,
                              Pageable pageable);

    Page<Study> findByUsers(User user, Pageable pageable);

}
