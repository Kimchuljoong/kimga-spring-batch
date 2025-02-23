package kr.co.kimga.batch.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, dateFormatter);
    }

    public static LocalDateTime toLocalDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, dateFormatter);
    }

    public static String toString(LocalDateTime dateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(dateTime);
    }
}
