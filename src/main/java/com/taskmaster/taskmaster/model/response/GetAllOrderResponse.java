package com.taskmaster.taskmaster.model.response;

import com.taskmaster.taskmaster.enums.OrderPaymentMethod;
import com.taskmaster.taskmaster.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllOrderResponse {

    private String orderCode;

    private String courseName;

    private String completedAt;

    private OrderPaymentMethod paymentMethod;

    private OrderStatus status;

    private Double totalPrice;

    private Double ppn;

    private Double totalTransfer;

}
