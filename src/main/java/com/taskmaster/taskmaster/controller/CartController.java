package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.AddCartItemRequest;
import com.taskmaster.taskmaster.model.response.GetCartItemsResponse;
import com.taskmaster.taskmaster.model.response.PagingResponse;
import com.taskmaster.taskmaster.model.response.PagingWebResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/carts")
public class CartController {

    private CartService cartService;

    @PostMapping(
        path = "/items",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> addCartItem(
        @Valid @RequestBody AddCartItemRequest request
    ) {
        cartService.addCart(request);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @DeleteMapping(
        path = "/items/{cartItemId}"
    )
    public WebResponse<String> deleteCartItemByIdAndUsername(
        @PathVariable(name = "cartItemId") Long cartItemId
    ) {
        cartService.deleteCartItem(cartItemId);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PagingWebResponse<List<GetCartItemsResponse>> getUserCartItems(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetCartItemsResponse> cartItemsResponsePage = cartService.getUserCartItems(page, size);
        List<GetCartItemsResponse> itemsResponses = cartItemsResponsePage.getContent();

        return PagingWebResponse.<List<GetCartItemsResponse>>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .data(itemsResponses)
            .paging(PagingResponse.builder()
                .currentPage(page)
                .size(size)
                .totalPage(cartItemsResponsePage.getTotalPages())
                .totalElement(cartItemsResponsePage.getTotalElements())
                .empty(cartItemsResponsePage.isEmpty())
                .first(cartItemsResponsePage.isFirst())
                .last(cartItemsResponsePage.isLast())
                .build())
            .build();
    }

}
