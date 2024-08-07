package com.taskmaster.taskmaster.model.request;

import com.taskmaster.taskmaster.enums.OrderPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayOrderRequest {

    @NotNull(message = "Payment method cannot be null")
    private OrderPaymentMethod paymentMethod;

}
