package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.Util.TaxUtil;
import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Order;
import com.taskmaster.taskmaster.model.response.GetOrdersResponse;
import com.taskmaster.taskmaster.model.response.CheckoutMidtransResponse;
import com.taskmaster.taskmaster.model.response.OrderItemResponse;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public GetOrdersResponse toGetAllUserOrdersResponse(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems()
            .stream()
            .map(items -> OrderItemResponse.builder()
                .studyName(items.getStudy().getName())
                .price(items.getPrice())
                .quantity(items.getQuantity())
                .build())
            .collect(Collectors.toList());

        return GetOrdersResponse.builder()
            .orderId(order.getId())
            .completedAt(TimeUtil.formatToString(order.getCompletedAt()))
            .paymentMethod(order.getPaymentMethod())
            .status(order.getStatus())
            .totalPrice(order.getOrderItems()
                .stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum())
            .ppn(order.getOrderItems()
                .stream()
                .mapToDouble(item -> TaxUtil.countPPN(item.getStudy()))
                .sum())
            .totalTransfer(order.getTotalTransfer())
            .orderItems(orderItemResponses)
            .build();
    }

    public CheckoutMidtransResponse toMidtransTransactionResponse(CheckoutMidtransResponse createdToken) {
        return CheckoutMidtransResponse.builder()
            .token(String.valueOf(createdToken.getToken()))
            .redirectUrl(String.valueOf(createdToken.getRedirectUrl()))
            .build();
    }

    public GetOrdersResponse toGetOrdersResponse(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getOrderItems()
            .stream()
            .map(items -> OrderItemResponse.builder()
                .studyName(items.getStudy().getName())
                .price(items.getPrice())
                .quantity(items.getQuantity())
                .build())
            .collect(Collectors.toList());

        return GetOrdersResponse.builder()
            .orderId(order.getId())
            .paymentMethod(order.getPaymentMethod())
            .status(order.getStatus())
            .totalPrice(order.getOrderItems()
                .stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum())
            .ppn(order.getOrderItems()
                .stream()
                .mapToDouble(item -> TaxUtil.countPPN(item.getStudy()))
                .sum())
            .totalTransfer(order.getTotalTransfer())
            .completedAt(TimeUtil.formatToString(order.getCompletedAt()))
            .orderItems(orderItemResponses)
            .build();
    }
}
