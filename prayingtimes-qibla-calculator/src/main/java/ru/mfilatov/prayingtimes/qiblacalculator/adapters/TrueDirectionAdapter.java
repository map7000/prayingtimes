/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.adapters;

import static ru.mfilatov.prayingtimes.models.Constants.KAABA_LOCATION;

import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.qiblacalculator.direction.GreatCircleCalculator;
import ru.mfilatov.prayingtimes.qiblacalculator.direction.PlanarApproximationCalculator;
import ru.mfilatov.prayingtimes.qiblacalculator.direction.VincentyCalculator;

/**
 * Calculates true direction/bearing between points using multiple methods with automatic fallback
 * based on accuracy requirements
 */
public class TrueDirectionAdapter {

  public enum CalculationMethod {
    VINCENTY, // Most precise (ellipsoidal Earth)
    GREAT_CIRCLE, // Spherical Earth (Haversine-based)
    PLANAR // Small distances only (Cartesian)
  }

  /**
   * Calculate true bearing from point
   *
   * @param point Starting location
   * @param method Preferred calculation method
   * @return Bearing in degrees (0° = true North, clockwise)
   * @throws IllegalArgumentException for invalid coordinates
   */
  public static double calculateBearing(GeoLocation point, CalculationMethod method) {

    validateCoordinates(point);

    return switch (method) {
      case VINCENTY -> new VincentyCalculator().calculateBearing(point, KAABA_LOCATION);
      case GREAT_CIRCLE -> new GreatCircleCalculator().calculateBearing(point, KAABA_LOCATION);
      case PLANAR -> new PlanarApproximationCalculator().calculateBearing(point, KAABA_LOCATION);
      default -> throw new IllegalArgumentException("Unsupported method");
    };
  }

  private static void validateCoordinates(GeoLocation p) {
    if (p == null) {
      throw new IllegalArgumentException("Locations cannot be null");
    }
    if (Double.isNaN(p.getLatitude()) || Double.isNaN(p.getLongitude())) {
      throw new IllegalArgumentException("Invalid coordinate values");
    }
  }
}
