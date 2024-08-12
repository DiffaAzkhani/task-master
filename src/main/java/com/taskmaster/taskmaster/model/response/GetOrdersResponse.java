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
public class GetOrdersResponse {

    private String orderId;

    private OrderStatus status;

    private OrderPaymentMethod paymentMethod;

    private int totalPrice;

    private Double ppn;

    private int totalTransfer;

    private String completedAt;

    private List<OrderItemResponse> orderItems;

}
