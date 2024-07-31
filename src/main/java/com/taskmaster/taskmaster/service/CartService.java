package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.model.request.AddCartRequest;
import com.taskmaster.taskmaster.model.response.GetCartItemsResponse;
import org.springframework.data.domain.Page;

public interface CartService {

    void addCart(AddCartRequest request);

    void deleteCartItem(String username, String studyCode);

    Page<GetCartItemsResponse> getAllCartItems(String username, int page, int size);

}
