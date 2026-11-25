/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TimezoneServiceTest {

  @Test
  void utcHasZeroOffset() {
    assertThat(TimezoneService.offsetHours("UTC", LocalDate.of(2026, 11, 25))).isZero();
  }

  @Test
  void daylightSavingIsAppliedForTheRequestedDate() {
    var summer = LocalDate.of(2026, 7, 15);
    var winter = LocalDate.of(2026, 1, 15);

    assertThat(TimezoneService.offsetHours("Europe/Berlin", summer)).isEqualTo(2.0);
    assertThat(TimezoneService.offsetHours("Europe/Berlin", winter)).isEqualTo(1.0);
  }

  @Test
  void fractionalOffsetsAreSupported() {
    assertThat(TimezoneService.offsetHours("Asia/Kolkata", LocalDate.of(2026, 11, 25)))
        .isEqualTo(5.5);
    assertThat(TimezoneService.offsetHours("Asia/Kathmandu", LocalDate.of(2026, 11, 25)))
        .isEqualTo(5.75);
  }

  @Test
  void negativeOffsetsAreSupported() {
    assertThat(TimezoneService.offsetHours("America/New_York", LocalDate.of(2026, 1, 15)))
        .isEqualTo(-5.0);
  }

  @Test
  void unknownTimezoneIsAValidatedClientError() {
    assertThatThrownBy(() -> TimezoneService.offsetHours("Not/AZone", LocalDate.of(2026, 1, 1)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Unknown timezone");
  }
}
