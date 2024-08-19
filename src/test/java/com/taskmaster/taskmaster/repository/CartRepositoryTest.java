package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Cart;
import com.taskmaster.taskmaster.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    private Cart testCart;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = User.builder()
            .username("TestUser")
            .password("testuser123")
            .firstName("test")
            .lastName("user")
            .email("test.email@example.com")
            .phone("12345")
            .build();

        userRepository.save(testUser);

        testCart = Cart.builder()
            .user(testUser)
            .build();

        cartRepository.save(testCart);
    }

    @AfterEach
    public void tearDown() {
        userRepository.delete(testUser);
        cartRepository.delete(testCart);
    }

    @Test
    public void givenUser_whenFindCartByUser_thenUserIsFound() {
        Cart foundCart = cartRepository.findByUser(testUser)
            .orElse(null);

        assertNotNull(foundCart);
        assertEquals(testCart, foundCart);
    }

    @Test
    public void givenUser_whenFindCartByUser_thenUserIsNotFound() {
        Cart foundCart = cartRepository.findByUser(null)
            .orElse(null);

        assertNull(foundCart);
    }

}
