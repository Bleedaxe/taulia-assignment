package tpp.taulia.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

  private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

  public static String dateToString(LocalDate date) {
    return date.format(DATE_FORMATTER);
  }

  public static LocalDate stringToDate(String dateString) {
    return LocalDate.parse(dateString, DATE_FORMATTER);
  }
}
