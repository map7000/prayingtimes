/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.direction;

import ru.mfilatov.prayingtimes.models.GeoLocation;

/**
 * Calculates the initial bearing between two points using planar approximation. This method is
 * suitable for short distances where Earth's curvature can be neglected.
 */
public class PlanarApproximation implements BearingCalculator{
  public double calculateBearing(GeoLocation point1, GeoLocation point2) {
    // Convert latitude and longitude from degrees to radians
    double lat1Rad = Math.toRadians(point1.getLatitude());
    double lon1Rad = Math.toRadians(point1.getLongitude());
    double lat2Rad = Math.toRadians(point2.getLatitude());
    double lon2Rad = Math.toRadians(point2.getLongitude());

    // Calculate differences in coordinates
    double deltaLon = lon2Rad - lon1Rad;
    double deltaLat = lat2Rad - lat1Rad;

    // Calculate bearing using planar approximation (arctangent of differences)
    double bearingRad = Math.atan2(deltaLon * Math.cos((lat1Rad + lat2Rad) / 2), deltaLat);

    // Convert bearing from radians to degrees
    double bearingDeg = Math.toDegrees(bearingRad);

    // Normalize to 0-360 degrees
    bearingDeg = (bearingDeg + 360) % 360;

    return bearingDeg;
  }
}
