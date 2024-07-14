package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddCouponRequest;
import com.taskmaster.taskmaster.model.response.AddCouponResponse;

public interface CouponService {
    AddCouponResponse addCoupon(AddCouponRequest request);

}
