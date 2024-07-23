package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.CancelOrderRequest;
import com.taskmaster.taskmaster.model.request.CreateOrderRequest;
import com.taskmaster.taskmaster.model.response.GetAllOrderResponse;
import com.taskmaster.taskmaster.model.response.OrderResponse;
import org.springframework.data.domain.Page;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    void cancelOrder(CancelOrderRequest request);

    Page<GetAllOrderResponse> getAllOrders(String username, int page, int size);

}
