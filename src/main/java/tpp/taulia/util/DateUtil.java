package tpp.taulia.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

  public static final String DATE_FORMAT_PATTERN = "dd-MM-yyyy";
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

  public static String dateToString(LocalDate date) {
    return date.format(DATE_FORMATTER);
  }
}
