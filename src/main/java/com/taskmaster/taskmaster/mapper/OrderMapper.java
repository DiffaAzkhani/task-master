package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.Util.TaxUtil;
import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Order;
import com.taskmaster.taskmaster.model.response.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
            .orderCode(order.getId())
            .username(order.getUser().getUsername())
            .courseName(order.getStudy().getName())
            .createdAt(TimeUtil.formatToString(order.getCreatedAt()))
            .completedAt(TimeUtil.formatToString(order.getCompletedAt()))
            .paymentDue(TimeUtil.formatToString(TimeUtil.generatePaymentDue(order.getPaymentMethod())))
            .paymentMethod(order.getPaymentMethod())
            .status(order.getStatus())
            .totalPrice(order.getStudy().getPrice())
            .ppn(TaxUtil.countPPN(order.getStudy()))
            .totalTransfer(order.getTotalTransfer())
            .build();
    }

}
