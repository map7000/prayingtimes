/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.model.aladhan;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record AladhanPrayerTimesRequest(
    @NotNull String date,
    @NotNull @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90") @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90") Double latitude,
    @NotNull @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180") @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180") Double longitude,
    Integer method,
    Shafaq shafaq,
    String tune,
    Integer school,
    Integer midnightMode,
    String timezonestring,
    Integer latitudeAdjustmentMethod,
    CalendarMethod calendarMethod,
    Boolean iso8601) {
  public enum Shafaq {
    GENERAL,
    AHBAR,
    ABYAD
  }

  public enum CalendarMethod {
    HJCoSA,
    UAQ,
    DIYANET,
    MATHEMATICAL
  }
}
