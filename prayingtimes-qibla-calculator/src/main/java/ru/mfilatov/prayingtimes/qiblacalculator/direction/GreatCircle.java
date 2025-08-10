/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.direction;

import ru.mfilatov.prayingtimes.models.GeoLocation;

/**
 * Calculates true bearing between points using great-circle navigation (Spherical Earth model with
 * Haversine formula)
 *
 * <p>Accuracy: ±0.1° for distances <20,000 km Reference: Sinnott, "Virtues of the Haversine" (1984)
 */
public class GreatCircle implements BearingCalculator{

  /**
   * Calculate initial bearing (forward azimuth) between two points
   *
   * @param start Starting location
   * @param end Target location
   * @return Bearing in degrees (0° = true North, clockwise)
   * @throws IllegalArgumentException for invalid coordinates
   */
  public double calculateBearing(GeoLocation start, GeoLocation end) {
    validateCoordinates(start, end);

    // Convert to radians
    double lat1 = Math.toRadians(start.getLatitude());
    double lon1 = Math.toRadians(start.getLongitude());
    double lat2 = Math.toRadians(end.getLatitude());
    double lon2 = Math.toRadians(end.getLongitude());

    // Calculate bearing components
    double y = Math.sin(lon2 - lon1) * Math.cos(lat2);
    double x =
        Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);

    // Calculate initial bearing
    double initial = Math.atan2(y, x);

    // Normalize to 0-360°
    return (Math.toDegrees(initial) + 360) % 360;
  }

  private static void validateCoordinates(GeoLocation start, GeoLocation end) {
    if (start == null || end == null) {
      throw new IllegalArgumentException("Locations cannot be null");
    }
    if (Double.isNaN(start.getLatitude())
        || Double.isNaN(start.getLongitude())
        || Double.isNaN(end.getLatitude())
        || Double.isNaN(end.getLongitude())) {
      throw new IllegalArgumentException("Invalid coordinate values");
    }
  }
}
