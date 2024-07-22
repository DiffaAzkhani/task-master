package com.taskmaster.taskmaster.Util;

import com.taskmaster.taskmaster.enums.OrderPaymentMethod;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
@Slf4j
public class TimeUtil {

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime getFormattedLocalDateTimeNow() {
        String formattedLocalDateTimeString = LocalDateTime.now().format(FORMATTER);
        return LocalDateTime.parse(formattedLocalDateTimeString, FORMATTER);
    }

    public String formatToString(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(FORMATTER) : null;
    }

    public LocalDateTime generatePaymentDue(OrderPaymentMethod paymentMethod) {
        LocalDateTime paymentDue;

        switch (paymentMethod) {
            case E_MONEY:
                paymentDue = LocalDateTime.now().plusMinutes(15);
                break;
            case CREDIT_CARD:
            case BANK_TRANSFER:
                paymentDue=LocalDateTime.now().plusDays(1);
                break;
            default:
                log.warn("Wrong payment method");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment method incorrect!");
        }

        return paymentDue;
    }

}
