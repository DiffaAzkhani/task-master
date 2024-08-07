package com.taskmaster.taskmaster.controller;

import com.midtrans.httpclient.error.MidtransError;
import com.taskmaster.taskmaster.configuration.midtrans.MidtransConfiguration;
import com.taskmaster.taskmaster.model.request.AfterPaymentsRequest;
import com.taskmaster.taskmaster.model.request.EnrollFreeStudiesRequest;
import com.taskmaster.taskmaster.model.request.MidtransTransactionRequest;
import com.taskmaster.taskmaster.model.response.CheckoutMidtransResponse;
import com.taskmaster.taskmaster.model.response.GetAllOrderResponse;
import com.taskmaster.taskmaster.model.response.PagingResponse;
import com.taskmaster.taskmaster.model.response.PagingWebResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    private final MidtransConfiguration midtransConfig;

    // API Path for Admin Role

    @GetMapping(
        path = "/{userId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PagingWebResponse<List<GetAllOrderResponse>> getAllUserOrders(
        @RequestParam(name = "userId") Long userId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetAllOrderResponse> orderResponsePage = orderService.getAllUserOrders(userId, page, size);
        List<GetAllOrderResponse> orderResponses = orderResponsePage.getContent();

        return PagingWebResponse.<List<GetAllOrderResponse>>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(orderResponses)
            .paging(PagingResponse.builder()
                .currentPage(page)
                .size(size)
                .totalPage(orderResponsePage.getTotalPages())
                .totalElement(orderResponsePage.getTotalElements())
                .empty(orderResponsePage.isEmpty())
                .first(orderResponsePage.isFirst())
                .last(orderResponsePage.isLast())
                .build())
            .build();
    }

    // API Path for User Role

    @PostMapping(
        path = "/checkout",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<CheckoutMidtransResponse>> completeCheckout(
        @Valid @RequestBody MidtransTransactionRequest request
    ) throws MidtransError {
        String encodedServerKey = midtransConfig.getEncodeServerKey();
        CheckoutMidtransResponse transactionResponse = orderService.completeCheckout(request);

        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedServerKey)
            .body(WebResponse.<CheckoutMidtransResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(transactionResponse)
                .build());
    }

    @GetMapping(
        path = "/me",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PagingWebResponse<List<GetAllOrderResponse>> getAllMyOrders(
        @RequestParam(name = "userId") Long userId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetAllOrderResponse> orderResponsePage = orderService.getAllUserOrders(userId, page, size);
        List<GetAllOrderResponse> orderResponses = orderResponsePage.getContent();

        return PagingWebResponse.<List<GetAllOrderResponse>>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(orderResponses)
            .paging(PagingResponse.builder()
                .currentPage(page)
                .size(size)
                .totalPage(orderResponsePage.getTotalPages())
                .totalElement(orderResponsePage.getTotalElements())
                .empty(orderResponsePage.isEmpty())
                .first(orderResponsePage.isFirst())
                .last(orderResponsePage.isLast())
                .build())
            .build();
    }

    @PostMapping(
        path = "/{orderId}/cancel"
    )
    public WebResponse<String> cancelOrder(
        @PathVariable(name = "orderId") Long orderId,
        @RequestParam(name = "username") String username
    ) {
        orderService.cancelOrder(orderId, username);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @PostMapping(
        path = "/midtrans-callback",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> handleAfterPayments(
        @Valid @RequestBody AfterPaymentsRequest request
    ) {
        String encodedServerKey = midtransConfig.getEncodeServerKey();
        orderService.handleAfterPayments(request);

        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.AUTHORIZATION, "Basic " + encodedServerKey)
            .body(WebResponse.<String>builder()
                .code(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .build());
    }

    @PostMapping(
        path = "/enroll-free",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> enrollFreeStudies(
        @Valid @RequestBody EnrollFreeStudiesRequest request
    ) {
        orderService.enrollFreeStudies(request);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

}
