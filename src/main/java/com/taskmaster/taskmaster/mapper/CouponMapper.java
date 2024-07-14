package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Coupon;
import com.taskmaster.taskmaster.model.response.AddCouponResponse;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public AddCouponResponse toAddCouponResponse(Coupon coupon) {
        return AddCouponResponse.builder()
            .code(coupon.getCode())
            .discount(coupon.getDiscount())
            .createdAt(TimeUtil.formatToString(coupon.getCreatedAt()))
            .expiredAt(TimeUtil.formatToString(coupon.getExpiredAt()))
            .build();
    }

}
