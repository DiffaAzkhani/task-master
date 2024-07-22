package com.taskmaster.taskmaster.model.request;

import com.taskmaster.taskmaster.enums.OrderPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    @NotBlank(message = "Username is required")
    @Size(max = 100, message = "Username should not be more than 100 characters")
    private String username;

    @NotBlank(message = "Code is required")
    @Size(max = 13, message = "Study Code should be 13 characters")
    private String studyCode;

    @NotNull(message = "Payment method cannot be null")
    private OrderPaymentMethod paymentMethod;

}
