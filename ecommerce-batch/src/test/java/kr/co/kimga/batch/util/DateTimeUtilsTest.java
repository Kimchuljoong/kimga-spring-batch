package kr.co.kimga.batch.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DateTimeUtilsTest {

    @Test
    void toLocalDate() {
        String date = "2024-09-21";

        LocalDate localDate = DateTimeUtils.toLocalDate(date);

        assertThat(localDate).isEqualTo(LocalDate.of(2024, 9, 21));
    }

    @Test
    void toLocalDateTime() {
        String dateTime = "2025-02-23 11:45:21.112";

        LocalDateTime localDateTime = DateTimeUtils.toLocalDateTime(dateTime);

        assertThat(localDateTime).isEqualTo(
                LocalDateTime.of(2025, 2, 23, 11, 45, 21, 112000000)
        );
    }

    @Test
    void toStringFromLocalDateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2025, 2, 23, 11, 45, 21, 112000000);

        String result = DateTimeUtils.toString(localDateTime);

        assertThat(result).isEqualTo("2025-02-23 11:45:21.112");
    }
}