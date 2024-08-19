package com.taskmaster.taskmaster.configuration.midtrans;

import com.midtrans.Config;
import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import com.taskmaster.taskmaster.entity.Order;
import com.taskmaster.taskmaster.model.request.ItemDetailsRequest;
import com.taskmaster.taskmaster.model.request.MidtransTransactionRequest;
import com.taskmaster.taskmaster.model.response.CheckoutMidtransResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class MidtransConfiguration {

    @Value("${midtrans.server-key}")
    private String SERVER_KEY;

//    @Value("${midtrans.client-key}")
    private String CLIENT_KEY;

    @Value("${midtrans.is-production}")
    private boolean IS_PRODUCTION;

    @Bean
    public Config midtransConfig() {
        return new Config(SERVER_KEY, CLIENT_KEY, IS_PRODUCTION);
    }

    public Map<String, Object> requestBody(Order order, MidtransTransactionRequest request) {
        Map<String, Object> params = new HashMap<>();
        int grossAmount = 0;

        List<Map<String, Object>> items = new ArrayList<>();
        for (ItemDetailsRequest itemDetailsRequest : request.getItem_details()) {
            int itemPriceWithTax = itemDetailsRequest.getPrice() + (itemDetailsRequest.getPrice() * 11) / 100;

            Map<String, Object> itemDetails = new HashMap<>();
            itemDetails.put("id", itemDetailsRequest.getId());
            itemDetails.put("name", itemDetailsRequest.getName());
            itemDetails.put("price", itemPriceWithTax);
            itemDetails.put("quantity", itemDetailsRequest.getQuantity());
            itemDetails.put("category", itemDetailsRequest.getCategory());
            itemDetails.put("merchant_name", "Task Master");

            int totalPrice = itemDetailsRequest.getPrice() * itemDetailsRequest.getQuantity();
            int totalPriceWithTax = totalPrice + (totalPrice * 11) / 100;
            grossAmount += totalPriceWithTax;

            items.add(itemDetails);
        }

        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", order.getId());
        transactionDetails.put("gross_amount", grossAmount);

        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("first_name", request.getCustomer_details().getFirst_name());
        customerDetails.put("last_name", request.getCustomer_details().getLast_name());
        customerDetails.put("email", request.getCustomer_details().getEmail());
        customerDetails.put("phone", request.getCustomer_details().getPhone());
        customerDetails.put("notes", request.getCustomer_details().getNotes());

        List<String> requiredFields = new ArrayList<>();
        requiredFields.add("first_name");
        requiredFields.add("phone");
        requiredFields.add("email");
        customerDetails.put("customer_details_required_fields", requiredFields);

        params.put("transaction_details", transactionDetails);
        params.put("item_details", items);
        params.put("customer_details", customerDetails);

        return params;
    }

    public CheckoutMidtransResponse createTransactionToken(Order order, MidtransTransactionRequest request) throws MidtransError {
        Map<String, Object> requestBody = requestBody(order, request);
        JSONObject jsonResponse = SnapApi.createTransaction(requestBody, midtransConfig());

        String token = jsonResponse.getString("token");
        String redirectUrl = jsonResponse.getString("redirect_url");

        CheckoutMidtransResponse transactionResponse = new CheckoutMidtransResponse();
        transactionResponse.setToken(token);
        transactionResponse.setRedirectUrl(redirectUrl);

        return transactionResponse;
    }

    public String getEncodeServerKey() {
        return Base64.getEncoder().encodeToString((SERVER_KEY + ":").getBytes());
    }

}
