package com.taskmaster.taskmaster.controller;

import com.midtrans.httpclient.error.MidtransError;
import com.taskmaster.taskmaster.configuration.midtrans.MidtransConfiguration;
import com.taskmaster.taskmaster.model.request.AfterPaymentsRequest;
import com.taskmaster.taskmaster.model.request.CancelOrderRequest;
import com.taskmaster.taskmaster.model.request.MidtransTransactionRequest;
import com.taskmaster.taskmaster.model.response.CheckoutMidtransResponse;
import com.taskmaster.taskmaster.model.response.GetAllOrderResponse;
import com.taskmaster.taskmaster.model.response.PagingResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping(
        path = "/cancel-order",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> cancelOrder(
        @Valid @RequestBody CancelOrderRequest request
    ) {
        orderService.cancelOrder(request);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<GetAllOrderResponse>> getAllORderResponse(
        @RequestParam(name = "username") String username,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetAllOrderResponse> orderResponsePage = orderService.getAllOrders(username, page, size);
        List<GetAllOrderResponse> orderResponses = orderResponsePage.getContent();

        return WebResponse.<List<GetAllOrderResponse>>builder()
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

}
