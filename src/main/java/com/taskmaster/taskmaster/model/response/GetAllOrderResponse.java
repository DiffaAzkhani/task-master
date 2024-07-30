package com.taskmaster.taskmaster.model.response;

import com.taskmaster.taskmaster.enums.OrderPaymentMethod;
import com.taskmaster.taskmaster.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllOrderResponse {

    private String orderId;

    private String courseName;

    private String completedAt;

    private OrderPaymentMethod paymentMethod;

    private OrderStatus status;

    private int totalPrice;

    private Double ppn;

    private int totalTransfer;

    private List<OrderItemResponse> orderItems;

}
