package com.taskmaster.taskmaster.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponResponse {

    private String code;

    private BigDecimal discount;

    private String expiredAt;

    private String createdAt;

    private String updatedAt;

}
