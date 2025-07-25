/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.adapters;

import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.qiblacalculator.direction.GreatCircle;
import ru.mfilatov.prayingtimes.qiblacalculator.direction.PlanarApproximation;
import ru.mfilatov.prayingtimes.qiblacalculator.direction.Vincenty;

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
   * Calculate true bearing from point1 to point2
   *
   * @param point1 Starting location
   * @param point2 Target location
   * @param method Preferred calculation method
   * @return Bearing in degrees (0° = true North, clockwise)
   * @throws IllegalArgumentException for invalid coordinates
   */
  public static double calculateBearing(
      GeoLocation point1, GeoLocation point2, CalculationMethod method) {

    validateCoordinates(point1, point2);

    return switch (method) {
      case VINCENTY -> Vincenty.calculateBearing(point1, point2);
      case GREAT_CIRCLE -> GreatCircle.calculateBearing(point1, point2);
      case PLANAR -> PlanarApproximation.calculateBearing(point1, point2);
      default -> throw new IllegalArgumentException("Unsupported method");
    };
  }

  private static void validateCoordinates(GeoLocation p1, GeoLocation p2) {
    if (p1 == null || p2 == null) {
      throw new IllegalArgumentException("Locations cannot be null");
    }
    if (Double.isNaN(p1.getLatitude())
        || Double.isNaN(p1.getLongitude())
        || Double.isNaN(p2.getLatitude())
        || Double.isNaN(p2.getLongitude())) {
      throw new IllegalArgumentException("Invalid coordinate values");
    }
  }
}
