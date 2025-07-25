/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.adapters;

import java.time.LocalDate;
import ru.mfilatov.prayingtimes.qiblacalculator.geomagnetic.HighOrderDeclination;
import ru.mfilatov.prayingtimes.qiblacalculator.geomagnetic.WorldMagneticModel;

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
      return HighOrderDeclination.calculate(lat, lon, date);
    }
  }
}
