package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Coupon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    private Coupon testCoupon;

    @BeforeEach
    void setUp() {
        testCoupon = Coupon.builder()
            .code("TESTCOUPON123")
            .discount(BigDecimal.valueOf(50))
            .build();

        couponRepository.save(testCoupon);
    }

    @AfterEach
    void tearDown() {
        couponRepository.delete(testCoupon);
    }

    @Test
    void givenCouponCode_whenFindCouponByCode_thenCouponIsFound() {
        Boolean existsCoupon = couponRepository.existsByCode(testCoupon.getCode());

        assertTrue(existsCoupon);
    }

    @Test
    void givenCouponCode_whenFindCouponByCode_thenCouponIsNotFound() {
        Boolean existsCoupon = couponRepository.existsByCode("WRONGCODE123");

        assertFalse(existsCoupon);
    }

}
