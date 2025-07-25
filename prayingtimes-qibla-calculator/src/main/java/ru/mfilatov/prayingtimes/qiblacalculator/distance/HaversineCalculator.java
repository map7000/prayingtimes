/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.distance;

import ru.mfilatov.prayingtimes.models.GeoLocation;

public class HaversineCalculator {
  // Earth's mean radius in meters (WGS84)
  private static final double EARTH_RADIUS = 6_371_000; // 6,371 km

  /**
   * Calculate great-circle distance between two points using Haversine formula
   *
   * @param point1 First geographic point
   * @param point2 Second geographic point
   * @return Distance between points in meters
   */
  public static double calculateDistance(GeoLocation point1, GeoLocation point2) {
    // Convert latitude/longitude from degrees to radians
    double lat1 = Math.toRadians(point1.getLatitude());
    double lon1 = Math.toRadians(point1.getLongitude());
    double lat2 = Math.toRadians(point2.getLatitude());
    double lon2 = Math.toRadians(point2.getLongitude());

    // Differences in coordinates
    double dLat = lat2 - lat1;
    double dLon = lon2 - lon1;

    // Haversine formula components
    double a =
        Math.pow(Math.sin(dLat / 2), 2)
            + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS * c;
  }
}
