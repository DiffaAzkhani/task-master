package com.taskmaster.taskmaster.Util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class TimeUtil {

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime getFormattedLocalDateTimeNow() {
        String formattedLocalDateTimeString = LocalDateTime.now().format(FORMATTER);
        return LocalDateTime.parse(formattedLocalDateTimeString, FORMATTER);
    }

    public String formatToString(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(FORMATTER) : null;
    }

}
