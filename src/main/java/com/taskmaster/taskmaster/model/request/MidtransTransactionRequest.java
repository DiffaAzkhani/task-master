package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidtransTransactionRequest {

    @Valid
    private List<ItemDetailsRequest> item_details;

    @Valid
    private CustomerDetailsRequest customer_details;

}
