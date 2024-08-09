package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddCartRequest;
import com.taskmaster.taskmaster.model.response.GetCartItemsResponse;
import org.springframework.data.domain.Page;

public interface CartService {

    void addCart(AddCartRequest request);

    void deleteCartItem(Long cartItemId, String username);

    Page<GetCartItemsResponse> getUserCartItems(Long userId, int page, int size);

}
