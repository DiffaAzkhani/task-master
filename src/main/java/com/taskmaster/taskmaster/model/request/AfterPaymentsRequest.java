package com.taskmaster.taskmaster.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AfterPaymentsRequest {

    private String transaction_time;

    private String transaction_status;

    private String transaction_id;

    private String status_message;

    private String status_code;

    private String signature_key;

    private String payment_type;

    private String order_id;

    private String merchant_id;

    private String masked_card;

    private String gross_amount;

    private String fraud_status;

    private String eci;

    private String currency;

    private String channel_response_message;

    private String channel_response_code;

    private String card_type;

    private String bank;

    private String approval_code;

}
