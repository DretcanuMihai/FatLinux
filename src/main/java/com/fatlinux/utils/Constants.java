package com.fatlinux.utils;

import java.time.format.DateTimeFormatter;

/**
 * class containing utility constants
 */
public class Constants {
    /**
     * a date time formatter used for date formatting
     */
    public static Double EPSILON = 0.0001;
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
}
