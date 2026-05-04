package uz.com.markethub.core.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {

    public static ZoneId systemZone = ZoneId.systemDefault();

    public static Instant parseMillisOrISO(String s) {
        if (s == null) return null;
        try {
            return Instant.ofEpochMilli(Long.parseLong(s));
        } catch (NumberFormatException e) {
            try {
                return Instant.parse(s);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public static LocalDate parseToLocalDate(String s) {
        Instant instant = parseMillisOrISO(s);
        return instant != null ? instant.atZone(systemZone).toLocalDate() : null;
    }

    public static LocalDate convertToLocalDate(Long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime convertToLocalDateTime(Long millis) {
        return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static long convertToMillis(LocalDateTime dateTime) {
        return dateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    public static long convertToMillis(LocalDate date) {
        return date
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }
}
