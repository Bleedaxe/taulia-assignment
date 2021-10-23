package tpp.taulia.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilTest {

  @Test
  void dateToString_withValidDate_shouldReturnIsoFormattedDate() {
    var date = LocalDate.of(2021, 10, 22);
    var formattedDate = DateUtil.dateToString(date);
    assertThat(formattedDate).isEqualTo("22-10-2021");
  }
}
