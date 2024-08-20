package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Cart;
import com.taskmaster.taskmaster.entity.CartItem;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private StudyRepository studyRepository;

    private List<CartItem> testCartItem;

    private User testUser;

    private Cart testCart;

    @BeforeEach
    void setUp() {
        testCartItem = new ArrayList<>();

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

        Study testStudy1 = Study.builder()
            .code("BLGY.11.11111")
            .name("Test Study1")
            .price(150000)
            .discount(0)
            .link("test-study1.mp4")
            .type(StudyType.PREMIUM)
            .level(StudyLevel.GRADE_12)
            .category(StudyCategory.PHYSICS)
            .build();

        studyRepository.save(testStudy1);

        Study testStudy2 = Study.builder()
            .code("BLGY.11.11112")
            .name("Test Study2")
            .price(150000)
            .discount(0)
            .link("test-study2.mp4")
            .type(StudyType.PREMIUM)
            .level(StudyLevel.GRADE_12)
            .category(StudyCategory.PHYSICS)
            .build();

        studyRepository.save(testStudy2);

        testCartItem.add(CartItem.builder()
            .cart(testCart)
            .study(testStudy1)
            .quantity(1)
            .build());

        testCartItem.add(CartItem.builder()
            .cart(testCart)
            .study(testStudy2)
            .quantity(1)
            .build());

        cartItemRepository.saveAll(testCartItem);
    }

    @AfterEach
    void tearDown() {
        cartRepository.delete(testCart);
        userRepository.delete(testUser);
    }

    @Test
    void givenUser_whenFindCartItemByUser_thenCartItemIsFoundWithPaging() {
        Pageable cartItemPage = PageRequest.of(0,2);
        Page<CartItem> foundCartItems = cartItemRepository.findByCart_User(testUser, cartItemPage);

        assertFalse(foundCartItems.getContent().isEmpty());
        assertEquals(2, foundCartItems.getContent().size());
        assertEquals(1, foundCartItems.getTotalPages());
        assertEquals(2, foundCartItems.getTotalElements());
        assertEquals(testCartItem.get(0).getId(), foundCartItems.getContent().get(0).getId());
    }

    @Test
    void givenUser_whenFindCartItemByUser_thenCartItemIsNotFoundWithPaging() {
        Pageable cartItemPage = PageRequest.of(0,2);
        Page<CartItem> foundCartItems = cartItemRepository.findByCart_User(null, cartItemPage);

        assertTrue(foundCartItems.getContent().isEmpty());
        assertEquals(0, foundCartItems.getContent().size());
        assertEquals(0, foundCartItems.getTotalPages());
        assertEquals(0, foundCartItems.getTotalElements());
    }

}
