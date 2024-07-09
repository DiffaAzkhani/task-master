package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddCouponRequest;
import com.taskmaster.taskmaster.model.response.CouponResponse;

public interface CouponService {
    CouponResponse addCoupon(AddCouponRequest request);

}
