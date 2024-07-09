package com.taskmaster.taskmaster.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCouponRequest {

    @NotNull(message = "Code is required")
    @Size(min = 5, max = 10, message = "Study Code should be between 5 and 10")
    private String code;

    @NotNull(message = "Discount is required")
    @Min(value = 0, message = "discount must be at least 0")
    private BigDecimal discount;

    @NotNull(message = "Expired Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime expiredAt;

}
