package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidtransTransactionRequest {

    private List<ItemDetailsRequest> item_details;

    private CustomerDetailsRequest customer_details;

}
