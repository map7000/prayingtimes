/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.utils;

import java.time.Duration;
import java.time.LocalTime;
import ru.mfilatov.prayingtimes.timecalculator.models.CalculationParameters;

public final class NightPortion {

  private NightPortion() {} // Prevent instantiation

  public static LocalTime calculateByMethod(
      LocalTime sunset,
      LocalTime sunrise,
      CalculationParameters.HighLatitudeMethod method,
      double portion) {
    if (sunset == null || sunrise == null) {
      return null;
    }

    Duration nightDuration =
        Duration.between(sunset, sunrise.plusHours(sunrise.isBefore(sunset) ? 24 : 0));

    return switch (method) {
      case MIDNIGHT_METHOD -> sunset.plus(nightDuration.dividedBy(2));
      case ONE_SEVENTH_METHOD -> sunset.plus(nightDuration.dividedBy(7));
      case MINIMAL_FAJR_ISHA -> sunset.plusMinutes((long) (portion * 60));
      default -> null;
    };
  }

  public static boolean needsAdjustment(double latitude) {
    return Math.abs(latitude) > 45; // Adjust for locations beyond 45° latitude
  }

  /**
   * Calculates night duration between Maghrib and Fajr
   *
   * @param maghrib Maghrib prayer time
   * @param fajr Next day's Fajr time
   * @return Duration of night in minutes
   */
  public static long getNightDurationMinutes(LocalTime maghrib, LocalTime fajr) {
    if (maghrib == null || fajr == null) {
      return 0;
    }

    if (maghrib.isBefore(fajr)) {
      return Duration.between(maghrib, fajr).toMinutes();
    } else {
      // Handle case where night spans midnight
      return Duration.between(maghrib, LocalTime.MAX).toMinutes()
          + Duration.between(LocalTime.MIN, fajr).toMinutes()
          + 1;
    }
  }

  /**
   * Calculates midpoint of night (for Midnight method)
   *
   * @param maghrib Maghrib time
   * @param fajr Next day's Fajr time
   * @return LocalTime representing midnight
   */
  public static LocalTime calculateMidnight(LocalTime maghrib, LocalTime fajr) {
    long nightMinutes = getNightDurationMinutes(maghrib, fajr);
    return maghrib.plusMinutes(nightMinutes / 2);
  }

  /**
   * Calculates 1/7th of night (for One Seventh method)
   *
   * @param maghrib Maghrib time
   * @param fajr Next day's Fajr time
   * @return LocalTime after 1/7th of night has passed
   */
  public static LocalTime calculateOneSeventh(LocalTime maghrib, LocalTime fajr) {
    long nightMinutes = getNightDurationMinutes(maghrib, fajr);
    return maghrib.plusMinutes(nightMinutes / 7);
  }

  /**
   * Calculates portion of night based on given factor
   *
   * @param maghrib Maghrib time
   * @param fajr Next day's Fajr time
   * @param portion Factor (0.0-1.0)
   * @return Time after portion of night has passed
   */
  public static LocalTime calculatePortion(LocalTime maghrib, LocalTime fajr, double portion) {
    if (portion < 0 || portion > 1) {
      throw new IllegalArgumentException("Portion must be between 0.0 and 1.0");
    }

    long nightMinutes = getNightDurationMinutes(maghrib, fajr);
    return maghrib.plusMinutes((long) (nightMinutes * portion));
  }

  /**
   * Checks if location requires high-latitude adjustments
   *
   * @param latitude Latitude in degrees
   * @return true if |latitude| >= 45°
   */
  public static boolean needsHighLatitudeAdjustment(double latitude) {
    return Math.abs(latitude) >= 45.0;
  }

  /** Gets night duration in hours for display purposes */
  public static double getNightDurationHours(LocalTime maghrib, LocalTime fajr) {
    return getNightDurationMinutes(maghrib, fajr) / 60.0;
  }
}
