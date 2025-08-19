/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.util;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class PrayerTimeUtils {
  private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  public static LocalTime parsePrayerTime(String timeString) {
    if (timeString == null || timeString.trim().isEmpty()) {
      return null;
    }
    return OffsetDateTime.parse(timeString, ISO_FORMATTER).toLocalTime();
  }

  public static String formatPrayerTime(OffsetDateTime time) {
    if (time == null) {
      return null;
    }
    return time.format(TIME_FORMATTER);
  }
}
