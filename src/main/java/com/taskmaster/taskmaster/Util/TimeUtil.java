package com.taskmaster.taskmaster.Util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

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

}
