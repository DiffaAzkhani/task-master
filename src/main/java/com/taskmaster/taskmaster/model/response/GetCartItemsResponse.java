package com.taskmaster.taskmaster.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCartItemsResponse {

    private Long id;

    private String productName;

    private Integer price;

    private int quantity;

}
