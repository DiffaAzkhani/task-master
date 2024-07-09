package com.taskmaster.taskmaster.repository;

import com.taskmaster.taskmaster.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Boolean existsByCode(String code);

}
