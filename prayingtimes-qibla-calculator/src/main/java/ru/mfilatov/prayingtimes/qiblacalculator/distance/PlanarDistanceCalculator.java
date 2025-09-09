/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.distance;

import ru.mfilatov.prayingtimes.models.GeoLocation;

/**
 * Calculates distances using planar approximation (Cartesian coordinates) Suitable only for small
 * distances (<10km) where Earth's curvature is negligible
 *
 * <p>Accuracy: <1% error within 10km, degrades rapidly beyond
 */
public class PlanarDistanceCalculator {
  // Mean Earth radius in meters (WGS84)
  private static final double EARTH_RADIUS = 6_371_000;

  // Degrees-to-meters conversion factors at equator
  private static final double METERS_PER_DEG_LAT = 111_320;
  private static final double METERS_PER_DEG_LON_AT_EQUATOR = 111_320;

  /**
   * Calculate planar approximation distance between two points
   *
   * @param point1 First location
   * @param point2 Second location
   * @return Distance in meters
   */
  public static double calculateDistance(GeoLocation point1, GeoLocation point2) {
    // Convert degrees to meters
    double dx = longitudeToMeters(point1, point2);
    double dy = latitudeToMeters(point1, point2);

    // Pythagorean theorem
    return Math.sqrt(dx * dx + dy * dy);
  }

  private static double latitudeToMeters(GeoLocation p1, GeoLocation p2) {
    return (p2.getLatitude() - p1.getLatitude()) * METERS_PER_DEG_LAT;
  }

  private static double longitudeToMeters(GeoLocation p1, GeoLocation p2) {
    // Adjust for latitude-dependent longitude compression
    double avgLat = Math.toRadians((p1.getLatitude() + p2.getLatitude()) / 2);
    double metersPerDegLon = METERS_PER_DEG_LON_AT_EQUATOR * Math.cos(avgLat);
    return (p2.getLongitude() - p1.getLongitude()) * metersPerDegLon;
  }

  /**
   * Check if planar approximation is suitable for given distance
   *
   * @param distanceMeters Proposed distance
   * @return true if error likely <1%
   */
  public static boolean isApproximationValid(double distanceMeters) {
    return distanceMeters < 10_000; // 10km threshold
  }
}
