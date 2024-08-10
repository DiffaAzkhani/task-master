package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddCartItemRequest;
import com.taskmaster.taskmaster.model.response.GetCartItemsResponse;
import org.springframework.data.domain.Page;

public interface CartService {

    void addCart(AddCartItemRequest request);

    void deleteCartItem(Long cartItemId);

    Page<GetCartItemsResponse> getUserCartItems(int page, int size);

}
