package com.issuetracker.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }
    
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : null;
    }
    
    public static LocalDate parseDate(String dateString) {
        return dateString != null ? LocalDate.parse(dateString, DATE_FORMATTER) : null;
    }
    
    public static LocalDateTime parseDateTime(String dateTimeString) {
        return dateTimeString != null ? LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER) : null;
    }
    
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
    
    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}