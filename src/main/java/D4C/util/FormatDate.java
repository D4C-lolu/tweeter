package D4C.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A simple utility class to format LocalDatetime objects
 */
public class FormatDate {
    final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static String formatDate(LocalDateTime ldt){
        String formattedString = ldt.format(CUSTOM_FORMATTER);
        return formattedString;
    }
}
