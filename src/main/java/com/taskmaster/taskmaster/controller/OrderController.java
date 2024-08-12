package com.taskmaster.taskmaster.controller;

import com.midtrans.httpclient.error.MidtransError;
import com.taskmaster.taskmaster.configuration.midtrans.MidtransConfiguration;
import com.taskmaster.taskmaster.model.request.AfterPaymentsRequest;
import com.taskmaster.taskmaster.model.request.MidtransTransactionRequest;
import com.taskmaster.taskmaster.model.response.CheckoutMidtransResponse;
import com.taskmaster.taskmaster.model.response.GetOrdersResponse;
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

    @GetMapping(
        path = "/{userId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PagingWebResponse<List<GetOrdersResponse>> getUserOrdersAdmin(
        @RequestParam(name = "userId") Long userId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetOrdersResponse> orderResponsePage = orderService.getUserOrdersAdmin(userId, page, size);
        List<GetOrdersResponse> orderResponses = orderResponsePage.getContent();

        return PagingWebResponse.<List<GetOrdersResponse>>builder()
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
    public PagingWebResponse<List<GetOrdersResponse>> getAllMyOrders(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetOrdersResponse> orderResponsePage = orderService.getAllUserOrders(page, size);
        List<GetOrdersResponse> orderResponses = orderResponsePage.getContent();

        return PagingWebResponse.<List<GetOrdersResponse>>builder()
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
        @PathVariable(name = "orderId") String orderId
    ) {
        orderService.cancelOrder(orderId);

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
        path = "/enroll-free"
    )
    public WebResponse<String> enrollFreeStudies(
        @RequestParam(name = "studyCode") String studyCode
    ) {
        orderService.enrollFreeStudies(studyCode);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PagingWebResponse<List<GetOrdersResponse>> getOrdersForAdmin(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetOrdersResponse> orderResponsePage = orderService.getOrdersForAdmin(page, size);
        List<GetOrdersResponse> orderResponses = orderResponsePage.getContent();

        return PagingWebResponse.<List<GetOrdersResponse>>builder()
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

}
