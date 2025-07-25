/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.direction;

import static ru.mfilatov.prayingtimes.models.Constants.EARTH_RADIUS;

import ru.mfilatov.prayingtimes.models.GeoLocation;

/**
 * Calculates true bearing between points using great-circle navigation (Spherical Earth model with
 * Haversine formula)
 *
 * <p>Accuracy: ±0.1° for distances <20,000 km Reference: Sinnott, "Virtues of the Haversine" (1984)
 */
public class GreatCircle {

  /**
   * Calculate initial bearing (forward azimuth) between two points
   *
   * @param start Starting location
   * @param end Target location
   * @return Bearing in degrees (0° = true North, clockwise)
   * @throws IllegalArgumentException for invalid coordinates
   */
  public static double calculateBearing(GeoLocation start, GeoLocation end) {
    validateCoordinates(start, end);

    // Convert to radians
    double φ1 = Math.toRadians(start.getLatitude());
    double λ1 = Math.toRadians(start.getLongitude());
    double φ2 = Math.toRadians(end.getLatitude());
    double λ2 = Math.toRadians(end.getLongitude());

    // Calculate bearing components
    double y = Math.sin(λ2 - λ1) * Math.cos(φ2);
    double x = Math.cos(φ1) * Math.sin(φ2) - Math.sin(φ1) * Math.cos(φ2) * Math.cos(λ2 - λ1);

    // Calculate initial bearing
    double θ = Math.atan2(y, x);

    // Normalize to 0-360°
    return (Math.toDegrees(θ) + 360) % 360;
  }

  /**
   * Calculate both initial and final bearings
   *
   * @return Array where [0]=initial bearing, [1]=final bearing
   */
  public static double[] calculateBothBearings(GeoLocation start, GeoLocation end) {
    double forward = calculateBearing(start, end);
    double reverse = calculateBearing(end, start);
    return new double[] {forward, reverse};
  }

  /**
   * Calculate great-circle distance with bearing
   *
   * @return Object array: [0]=distance in meters, [1]=bearing in degrees
   */
  public static Object[] calculateDistanceWithBearing(GeoLocation start, GeoLocation end) {
    // Haversine distance calculation
    double φ1 = Math.toRadians(start.getLatitude());
    double φ2 = Math.toRadians(end.getLatitude());
    double Δφ = φ2 - φ1;
    double Δλ = Math.toRadians(end.getLongitude() - start.getLongitude());

    double a =
        Math.sin(Δφ / 2) * Math.sin(Δφ / 2)
            + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = EARTH_RADIUS * c;

    // Bearing calculation
    double bearing = calculateBearing(start, end);

    return new Object[] {distance, bearing};
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
