package com.rxh.utils;

import java.time.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/9/25
 * Time: 10:09
 * Project: Management
 * Package: com.rxh.utils
 */
public class CalendarUtils {

    private static ZoneId zone = ZoneId.systemDefault();

    public static Date localTime2Date(LocalTime localTime) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zone);
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date localDate2Date(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(zone);
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(zone);
        return Date.from(zonedDateTime.toInstant());
    }

    public static LocalTime date2LocalTime(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalTime();
    }

    public static LocalDate date2LocalDate(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, zone);
    }
}