package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Order;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.enums.OrderPaymentMethod;
import com.taskmaster.taskmaster.enums.OrderStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    private Order testOrder;

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

        for (int i = 1; i <= 3; i++) {
            testOrder = Order.builder()
                .id("INV-20240818-0000" + i)
                .createdAt(TimeUtil.getFormattedLocalDateTimeNow())
                .paymentMethod(OrderPaymentMethod.BANK_TRANSFER)
                .status(OrderStatus.PROCESSING)
                .totalTransfer(150000)
                .user(testUser)
                .build();

            orderRepository.save(testOrder);
        }
    }

    @AfterEach
    public void tearDown() {
        orderRepository.delete(testOrder);
    }

    @Test
    public void givenUserAndOrderId_whenFindOrderByUserAndOrderId_thenOrderIsFound() {
        Order foundOrder = orderRepository.findByUserAndId(testUser, testOrder.getId())
            .orElse(null);

        assertNotNull(foundOrder);
        assertEquals(testOrder, foundOrder);
    }

    @Test
    public void givenUserAndOrderId_whenFindOrderByUserAndOrderId_thenOrderIsNotFound() {
        Order foundOrder = orderRepository.findByUserAndId(testUser, "INV-xxxxxxxx-00001")
            .orElse(null);

        assertNull(foundOrder);
    }

    @Test
    public void givenUser_whenFindOrderByUser_thenOrderIsFoundWithPagination() {
        Pageable orderPage = PageRequest.of(0,2);
        Page<Order> foundOrder = orderRepository.findByUser(testUser, orderPage);

        assertEquals(2, foundOrder.getContent().size());
        assertEquals(2, foundOrder.getTotalPages());
        assertEquals(3, foundOrder.getTotalElements());
        assertEquals("INV-20240818-00001", foundOrder.getContent().get(0).getId());
    }

    @Test
    public void givenUser_whenFindOrderByUser_thenOrderIsNotFoundWithPagination() {
        Pageable orderPage = PageRequest.of(0,3);
        Page<Order> foundOrder = orderRepository.findByUser(null, orderPage);

        assertEquals(0, foundOrder.getContent().size());
        assertEquals(0, foundOrder.getTotalPages());
        assertEquals(0, foundOrder.getTotalElements());
        assertTrue(foundOrder.getContent().isEmpty());
    }

}
