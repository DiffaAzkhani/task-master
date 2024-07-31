package com.taskmaster.taskmaster.controller;

import com.taskmaster.taskmaster.model.request.AddCartRequest;
import com.taskmaster.taskmaster.model.response.GetCartItemsResponse;
import com.taskmaster.taskmaster.model.response.PagingResponse;
import com.taskmaster.taskmaster.model.response.WebResponse;
import com.taskmaster.taskmaster.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private CartService cartService;

    @PostMapping(
        path = "/add-cart",
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> addCart(
        @Valid @RequestBody AddCartRequest request
    ) {
        cartService.addCart(request);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @DeleteMapping(
        path = "/{username}/{studyCode}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteCartItem(
        @PathVariable(name = "username") String username,
        @PathVariable(name = "studyCode") String studyCode
    ) {
        cartService.deleteCartItem(username, studyCode);

        return WebResponse.<String>builder()
            .code(HttpStatus.OK.value())
            .message(HttpStatus.OK.getReasonPhrase())
            .build();
    }

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<GetCartItemsResponse>> getAllCartItems(
        @RequestParam(name = "username") String username,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Page<GetCartItemsResponse> cartItemsResponsePage = cartService.getAllCartItems(username, page, size);
        List<GetCartItemsResponse> itemsResponses = cartItemsResponsePage.getContent();

        return WebResponse.<List<GetCartItemsResponse>>builder()
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
