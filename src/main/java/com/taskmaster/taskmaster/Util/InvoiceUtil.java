package com.taskmaster.taskmaster.Util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@UtilityClass
public class InvoiceUtil {

    private final AtomicLong orderCounter = new AtomicLong(1);

    private LocalDate lastUpdatedDate = LocalDate.now();

    public static String invoiceGenerator() {
        long orderNumber = orderCounter.getAndIncrement();
        String formattedOrderNumber = String.format("%05d",orderNumber);

        return String.format("INV-%4d%02d%02d-%s",
            lastUpdatedDate.getYear(),
            lastUpdatedDate.getMonthValue(),
            lastUpdatedDate.getDayOfMonth(),
            formattedOrderNumber);
    }

    private void resetNumberIfDaysChange() {
        boolean isYesterday = lastUpdatedDate.isBefore(LocalDate.now());

        if (isYesterday) {
            orderCounter.set(1);
            lastUpdatedDate = LocalDate.now();
        }
    }

}
