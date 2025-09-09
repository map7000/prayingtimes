/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.utils;

import static org.apache.commons.math3.util.FastMath.*;

import java.time.LocalDate;

public class PolarUtils {

  // Check if location is in polar day (24h daylight) on given date
  public static boolean isPolarDay(LocalDate date, double latitude) {
    double declination = calculateSunDeclination(date);
    return (latitude > 0 && 90 - latitude <= declination)
        || // Northern hemisphere
        (latitude < 0 && -90 - latitude >= declination); // Southern hemisphere
  }

  // Check if location is in polar night (24h darkness) on given date
  public static boolean isPolarNight(LocalDate date, double latitude) {
    double declination = calculateSunDeclination(date);
    return (latitude > 0 && 90 - latitude <= -declination)
        || // Northern hemisphere
        (latitude < 0 && -90 - latitude >= -declination); // Southern hemisphere
  }

  private static double calculateSunDeclination(LocalDate date) {
    // Reuse your existing declination calculation from MWLCalculationStrategy
    int dayOfYear = date.getDayOfYear();
    return Math.toDegrees(0.409 * sin((2 * PI / 368) * (dayOfYear - 81)));
  }

  // Get polar day/night duration in days
  public static int getPolarDuration(double latitude) {
    latitude = abs(latitude);
    if (latitude < 66.5) return 0;
    return (int) round((latitude - 66.5) * 0.5 * 365 / 24);
  }
}
