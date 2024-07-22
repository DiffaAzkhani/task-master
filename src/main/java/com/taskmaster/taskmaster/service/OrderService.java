package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.CancelOrderRequest;
import com.taskmaster.taskmaster.model.request.CreateOrderRequest;
import com.taskmaster.taskmaster.model.response.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    void cancelOrder(CancelOrderRequest request);

}
