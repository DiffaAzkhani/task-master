package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Coupon;
import com.taskmaster.taskmaster.model.request.AddCouponRequest;
import com.taskmaster.taskmaster.model.response.CouponResponse;
import com.taskmaster.taskmaster.repository.CouponRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@AllArgsConstructor
@Service
public class CouponServiceImpl implements CouponService {

    private CouponRepository couponRepository;

    @Override
    @Transactional
    public CouponResponse addCoupon(AddCouponRequest request) {
        log.info("Recived add coupon request: {}", request);

        if (Boolean.TRUE.equals(couponRepository.existsByCode(request.getCode()))) {
            log.info("Coupon with code: {}, already exists!", request.getCode());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Coupon already exists");
        }

        Coupon coupon = Coupon.builder()
            .code(request.getCode())
            .discount(request.getDiscount())
            .expiredAt(request.getExpiredAt())
            .build();

        log.info("Success to save coupon with code: {}", request.getCode());
        couponRepository.save(coupon);

        return toCourseResponse(coupon);
    }

    private CouponResponse toCourseResponse(Coupon coupon) {
        return CouponResponse.builder()
            .code(coupon.getCode())
            .discount(coupon.getDiscount())
            .expiredAt(TimeUtil.formatToString(coupon.getExpiredAt()))
            .createdAt(TimeUtil.formatToString(coupon.getCreatedAt()))
            .updatedAt(TimeUtil.formatToString(coupon.getUpdatedAt()))
            .build();
    }
}
