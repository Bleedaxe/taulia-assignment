package tpp.taulia.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilTest {

  @Test
  void dateToString_withValidDate_shouldReturnDateInExpectedFormat() {
    var date = LocalDate.of(2021, 10, 22);
    var formattedDate = DateUtil.dateToString(date);
    assertThat(formattedDate).isEqualTo("22-10-2021");
  }

  @Test
  void stringToDate_withValidDateString_shouldCreateValidLocalDate() {
    String dateString = "22-10-2021";
    var fromString = DateUtil.stringToDate(dateString);
    assertThat(fromString).isEqualTo(LocalDate.of(2021, 10, 22));
  }
}
