/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.adjustments;

import java.time.LocalDate;
import java.time.LocalTime;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.models.PrayerTimes;
import ru.mfilatov.prayingtimes.timecalculator.models.CalculationParameters;
import ru.mfilatov.prayingtimes.timecalculator.utils.PolarUtils;

public class PolarAdjuster {

  public static void adjust(
      PrayerTimes times, LocalDate date, GeoLocation location, CalculationParameters params) {

    if (!PolarUtils.isPolarDay(date, location.getLatitude())
        && !PolarUtils.isPolarNight(date, location.getLatitude())) {
      return;
    }

    if (PolarUtils.isPolarDay(date, location.getLatitude())) {
      handlePolarDay(times, params);
    } else {
      handlePolarNight(times, params);
    }
  }

  private static void handlePolarDay(PrayerTimes times, CalculationParameters params) {
    // During 24h daylight:
    // - Fajr: Fixed time before astronomical noon
    // - Sunrise/Sunset: Not applicable
    // - Maghrib/Isha: Fixed time after astronomical noon

    LocalTime noon = times.getDhuhr();

    switch (params.getPolarDayMethod()) {
      case FIXED_WINDOWS:
        times.setFajr(noon.minusHours(6));
        times.setSunrise(null);
        times.setMaghrib(noon.plusHours(6));
        times.setSunset(null);
        times.setIsha(noon.plusHours(7));
        break;

      case NEAREST_DAY:
        // Use times from nearest date with normal day/night
        // (Would require pre-calculated reference times)
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + params.getPolarDayMethod());
    }
  }

  private static void handlePolarNight(PrayerTimes times, CalculationParameters params) {
    // During 24h darkness:
    // - All prayers based on clock times
    // - Common approaches:
    //   a) Use times from nearest normal day
    //   b) Fixed schedule based on last known times

    switch (params.getPolarNightMethod()) {
      case FIXED_INTERVALS:
        times.setFajr(LocalTime.of(6, 0));
        times.setSunrise(null);
        times.setDhuhr(LocalTime.of(12, 0));
        times.setAsr(LocalTime.of(14, 0));
        times.setMaghrib(LocalTime.of(18, 0));
        times.setIsha(LocalTime.of(19, 0));
        break;

      case PolarNightMethod.LAST_VALID_TIMES:
        // Maintain previous prayer times
        // (Requires storing previous day's times)
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + params.getPolarNightMethod());
    }
  }

  public enum PolarDayMethod {
    FIXED_WINDOWS, // Fixed hours relative to noon
    NEAREST_DAY // Use times from closest normal day
  }

  public enum PolarNightMethod {
    FIXED_INTERVALS, // Fixed clock times
    LAST_VALID_TIMES // Continue using last valid times
  }
}
