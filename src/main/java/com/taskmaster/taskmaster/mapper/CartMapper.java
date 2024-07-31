package com.taskmaster.taskmaster.mapper;

import com.taskmaster.taskmaster.entity.CartItem;
import com.taskmaster.taskmaster.model.response.GetCartItemsResponse;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public GetCartItemsResponse toCartItemsResponse(CartItem cartItem) {
        return GetCartItemsResponse.builder()
            .id(cartItem.getId())
            .productName(cartItem.getStudy().getName())
            .quantity(cartItem.getQuantity())
            .price(cartItem.getStudy().getPrice())
            .build();
    }

}
