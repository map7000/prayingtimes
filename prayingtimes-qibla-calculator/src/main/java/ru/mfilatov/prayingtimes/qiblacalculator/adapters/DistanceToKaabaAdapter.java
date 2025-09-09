/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.adapters;

import static ru.mfilatov.prayingtimes.models.Constants.KAABA_LOCATION;

import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.qiblacalculator.distance.HaversineCalculator;
import ru.mfilatov.prayingtimes.qiblacalculator.distance.PlanarDistanceCalculator;
import ru.mfilatov.prayingtimes.qiblacalculator.distance.VincentyDistanceCalculator;

/** Adapter for calculating distance to Kaaba using different methods */
public class DistanceToKaabaAdapter {

  public enum DistanceMethod {
    HAVERSINE, // Spherical Earth approximation
    VINCENTY, // Geodesic on ellipsoid (WGS84)
    PLANAR // Simple planar approximation (small distances only)
  }

  /**
   * Calculate distance to Kaaba using configured method
   *
   * @param location Observer's location
   * @return Distance in meters
   */
  public static double calculateDistance(GeoLocation location, DistanceMethod method) {
    return switch (method) {
      case VINCENTY -> VincentyDistanceCalculator.calculateDistance(KAABA_LOCATION, location);
      case PLANAR -> PlanarDistanceCalculator.calculateDistance(KAABA_LOCATION, location);
      case HAVERSINE -> HaversineCalculator.calculateDistance(KAABA_LOCATION, location);
      default -> throw new IllegalArgumentException("Unsupported method");
    };
  }
}
