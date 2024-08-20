package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Role;
import com.taskmaster.taskmaster.enums.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role  testRole;

    @BeforeEach
    void setUp() {
        testRole = Role.builder()
            .name(UserRole.USER)
            .build();

        roleRepository.save(testRole);
    }

    @AfterEach
    void tearDown() {
        roleRepository.delete(testRole);
    }

    @Test
    void givenRoleName_WhenFindByName_thenRoleIsFound() {
        Role foundRole = roleRepository.findByName(UserRole.USER)
            .orElse(null);

        assertNotNull(foundRole);
        assertEquals(testRole, foundRole);
    }

}
