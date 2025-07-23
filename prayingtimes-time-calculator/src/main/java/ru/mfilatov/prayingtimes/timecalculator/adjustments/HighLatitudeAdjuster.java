/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.adjustments;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import ru.mfilatov.prayingtimes.timecalculator.models.CalculationParameters;
import ru.mfilatov.prayingtimes.timecalculator.models.GeoLocation;
import ru.mfilatov.prayingtimes.timecalculator.models.PrayerTimes;
import ru.mfilatov.prayingtimes.timecalculator.utils.NightPortion;

/** Handles prayer time adjustments for locations above 45° latitude */
public class HighLatitudeAdjuster {

  public static void adjust(
      PrayerTimes times, LocalDate date, GeoLocation location, CalculationParameters params) {

    if (!needsAdjustment(location.getLatitude())) {
      return;
    }

    switch (params.getHighLatitudeMethod()) {
      case ANGLE_BASED:
        applyAngleBasedAdjustment(times, date, location, params);
        break;
      case MIDNIGHT_METHOD:
        applyMidnightMethod(times);
        break;
      case ONE_SEVENTH_METHOD:
        applyOneSeventhMethod(times);
        break;
      case MINIMAL_FAJR_ISHA:
        applyMinimalTimes(times, params);
        break;
    }
  }

  private static boolean needsAdjustment(double latitude) {
    return Math.abs(latitude) >= 45.0;
  }

  private static void applyAngleBasedAdjustment(
      PrayerTimes times, LocalDate date, GeoLocation location, CalculationParameters params) {
    // Only adjust if normal calculation failed
    if (times.getFajr() == null) {
      times.setFajr(
          calculateRecommendedTime(times.getSunrise(), -params.getFajrAngle(), date, location));
    }

    if (times.getIsha() == null) {
      times.setIsha(
          calculateRecommendedTime(times.getMaghrib(), params.getIshaAngle(), date, location));
    }
  }

  private static LocalTime calculateRecommendedTime(
      LocalTime baseTime, double angle, LocalDate date, GeoLocation location) {
    if (baseTime == null) return null;

    ZonedDateTime zdt = ZonedDateTime.of(date, baseTime, ZoneId.of(location.getTimezone()));

    long nightSeconds = ChronoUnit.SECONDS.between(zdt.withHour(0).withMinute(0), zdt);

    double adjustmentHours = (angle / 15.0) * (nightSeconds / 3600.0);
    return baseTime.plusMinutes((long) (adjustmentHours * 60));
  }

  private static void applyMidnightMethod(PrayerTimes times) {
    LocalTime midnight = NightPortion.calculateMidnight(times.getMaghrib(), times.getSunrise());

    if (times.getFajr() == null) {
      times.setFajr(midnight.minusMinutes(90)); // 1.5 hours before midnight
    }

    if (times.getIsha() == null) {
      times.setIsha(midnight.plusMinutes(90)); // 1.5 hours after midnight
    }
  }

  private static void applyOneSeventhMethod(PrayerTimes times) {
    LocalTime portion = NightPortion.calculateOneSeventh(times.getMaghrib(), times.getSunrise());

    if (times.getFajr() == null) {
      times.setFajr(portion);
    }

    if (times.getIsha() == null) {
      times.setIsha(
          times
              .getMaghrib()
              .plusMinutes(
                  (int)
                      (NightPortion.getNightDurationMinutes(times.getMaghrib(), times.getSunrise())
                          * 6.0
                          / 7.0)));
    }
  }

  private static void applyMinimalTimes(PrayerTimes times, CalculationParameters params) {
    if (times.getFajr() == null && times.getSunrise() != null) {
      times.setFajr(times.getSunrise().minusMinutes(params.getMinimalFajrMinutes()));
    }

    if (times.getIsha() == null && times.getMaghrib() != null) {
      times.setIsha(times.getMaghrib().plusMinutes(params.getMinimalIshaMinutes()));
    }
  }

  /** Validates if adjustment was successful */
  public static boolean isValidAdjustment(PrayerTimes times) {
    return times.getFajr() != null
        && times.getIsha() != null
        && times.getFajr().isBefore(times.getSunrise())
        && times.getIsha().isAfter(times.getMaghrib());
  }
}
