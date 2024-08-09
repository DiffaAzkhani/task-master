package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.AddCouponRequest;
import com.taskmaster.taskmaster.model.response.AddCouponResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/coupons")
public class CouponController {

    private final CouponService couponService;

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<AddCouponResponse>> addCoupon(
        @Valid @RequestBody AddCouponRequest request
    ) {
        AddCouponResponse couponResponse = couponService.addCoupon(request);

        return ResponseEntity.status(HttpStatus.OK)
            .body(WebResponse.<AddCouponResponse>builder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(couponResponse)
                .build());
    }

}
