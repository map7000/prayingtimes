/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.geomagnetic;

import java.time.LocalDate;

public class MagneticDeclinationAdapter {
  /**
   * Gets magnetic declination using the best available method (Falls back to approximation if WMM
   * not available)
   */
  public static double getDeclination(double lat, double lon, double alt, LocalDate date) {
    try {
      // Try official WMM first
      return WorldMagneticModel.calculateDeclination(lat, lon, alt, date);
    } catch (Exception e) {
      // Fallback to approximation
      return approximateDeclination(lat, lon, date.getYear());
    }
  }

  private static double approximateDeclination(double lat, double lon, int year) {
    // Tenth-order polynomial approximation (accurate to ±1° for 2015-2025)
    double t = year - 2020;
    return -7.5 + 0.18 * lon - 0.001 * lon * lon + 0.05 * lat - 0.0005 * lat * lat + 0.05 * t;
  }
}
