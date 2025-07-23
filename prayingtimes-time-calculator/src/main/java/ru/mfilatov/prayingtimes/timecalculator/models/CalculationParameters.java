/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.models;

import lombok.Builder;
import lombok.Data;
import ru.mfilatov.prayingtimes.timecalculator.adjustments.PolarAdjuster;

@Data
@Builder
public class CalculationParameters {

  // Calculation Method
  private CalculationMethod method;

  // Angle-Based Parameters
  private double fajrAngle; // Degrees
  private double ishaAngle; // Degrees
  private double maghribAngle; // Degrees (alternative to fixed minutes)
  private double ishaInterval; // Minutes after Maghrib (alternative to angle)

  // Asr Calculation
  private final AsrJuristicMethod asrMethod;
  private final double asrShadowFactor; // Used when asrMethod = CUSTOM

  // Minute Based Adjustments
  private final int fajrAdjustment;
  private final int sunriseAdjustment;
  private final int dhuhrAdjustment;
  private final int asrAdjustment;
  private final int maghribAdjustment;
  private final int ishaAdjustment;

  // High Latitude Adjustment
  private final HighLatitudeMethod highLatitudeMethod;
  private final double highLatitudeNightPortion;

  // Polar Region Handling
  private final PolarAdjuster.PolarDayMethod polarDayMethod =
      PolarAdjuster.PolarDayMethod.FIXED_WINDOWS;
  private final PolarAdjuster.PolarNightMethod polarNightMethod =
      PolarAdjuster.PolarNightMethod.FIXED_INTERVALS;
  private final int polarDayFajrMinutes; // Minutes before noon
  private final int polarDayIshaMinutes; // Minutes after noon
  private final int polarNightFajrHour; // Fixed clock time
  private final int polarNightIshaHour;

  // Daylight Saving
  private boolean daylightSavingEnabled;
  private int dstOffsetMinutes;

  // Calculation Methods
  public enum CalculationMethod {
    MWL, // Muslim World League
    ISNA, // Islamic Society of North America
    EGYPT, // Egyptian General Authority
    MAKKAH, // Umm al-Qura, Makkah
    KARACHI, // University of Islamic Sciences, Karachi
    TEHRAN, // Institute of Geophysics, University of Tehran
    JAFARI, // Shia Ithna-Ashari (Jafari)
    CUSTOM // Custom angles/parameters
  }

  public enum AsrJuristicMethod {
    SHAFII, // Shadow = 1
    HANAFI, // Shadow = 2
    CUSTOM // Use asrShadowFactor
  }

  public enum HighLatitudeMethod {
    NONE, // No adjustment
    ANGLE_BASED, // Use recommended angles (default)
    MIDNIGHT_METHOD, // 1/2 of night duration
    ONE_SEVENTH_METHOD, // 1/7 of night duration
    MINIMAL_FAJR_ISHA // Minimal Fajr/Isha times (fixed windows)
  }

  // Polar Region Methods
  public enum PolarDayMethod {
    FIXED_WINDOWS, // Fixed hours relative to noon
    NEAREST_DAY // Use times from closest normal day
  }

  public enum PolarNightMethod {
    FIXED_INTERVALS, // Fixed clock times
    LAST_VALID_TIMES // Continue using last valid times
  }

  // Default configurations for known methods
  public static CalculationParameters getDefault(CalculationMethod method) {
    return switch (method) {
      case MWL ->
          CalculationParameters.builder()
              .method(CalculationMethod.MWL)
              .fajrAngle(18.0)
              .ishaAngle(17.0)
              .asrMethod(AsrJuristicMethod.SHAFII)
              .highLatitudeMethod(HighLatitudeMethod.ANGLE_BASED)
              .build();

      case ISNA ->
          CalculationParameters.builder()
              .method(CalculationMethod.ISNA)
              .fajrAngle(15.0)
              .ishaAngle(15.0)
              .asrMethod(AsrJuristicMethod.SHAFII)
              .build();

      default -> throw new IllegalArgumentException("Unsupported calculation method");
    };
  }
}
