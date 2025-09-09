/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.adapters;

import java.time.LocalDate;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.qiblacalculator.geomagnetic.HighOrderDeclination;
import ru.mfilatov.prayingtimes.qiblacalculator.geomagnetic.WorldMagneticModel;

public class MagneticDeclinationAdapter {
  public enum CalculationMethod {
    WORLD_MAGNETIC_MODEL,
    POLYNOMIAL_APPROXIMATION
  }

  /**
   * Gets magnetic declination using the best available method (Falls back to approximation if WMM
   * not available)
   */
  public static double getDeclination(
      GeoLocation location, LocalDate date, CalculationMethod method) {
    return switch (method) {
      case WORLD_MAGNETIC_MODEL -> WorldMagneticModel.calculateDeclination(location, date);
      case POLYNOMIAL_APPROXIMATION -> HighOrderDeclination.calculateDeclination(location, date);
      default -> throw new IllegalArgumentException("Unsupported method");
    };
  }
}
