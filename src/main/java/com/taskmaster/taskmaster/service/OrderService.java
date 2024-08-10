package com.taskmaster.taskmaster.service;

import com.midtrans.httpclient.error.MidtransError;
import com.taskmaster.taskmaster.model.request.AfterPaymentsRequest;
import com.taskmaster.taskmaster.model.request.MidtransTransactionRequest;
import com.taskmaster.taskmaster.model.response.CheckoutMidtransResponse;
import com.taskmaster.taskmaster.model.response.GetAllOrderResponse;
import org.springframework.data.domain.Page;

public interface OrderService {

    CheckoutMidtransResponse completeCheckout(MidtransTransactionRequest request) throws MidtransError;

    void cancelOrder(String orderId);

    Page<GetAllOrderResponse> getAllUserOrders(int page, int size);

    Page<GetAllOrderResponse> getUserOrdersAdmin(Long userId, int page, int size);

    void handleAfterPayments(AfterPaymentsRequest request);

    void enrollFreeStudies(String studyCode);

}
