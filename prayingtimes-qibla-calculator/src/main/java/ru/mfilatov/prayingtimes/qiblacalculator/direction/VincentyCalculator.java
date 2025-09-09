/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.direction;

import ru.mfilatov.prayingtimes.models.GeoLocation;

/**
 * High-precision Qibla direction calculator using Vincenty's inverse formula on the WGS84 ellipsoid
 * model of the Earth.
 */
public class VincentyCalculator implements BearingCalculator {
  // WGS84 ellipsoid parameters
  private static final double SEMI_MAJOR_AXIS = 6378137.0; // meters
  private static final double FLATTENING = 1 / 298.257223563;
  private static final double SEMI_MINOR_AXIS = SEMI_MAJOR_AXIS * (1 - FLATTENING);
  private static final double CONVERGENCE_TOLERANCE = 1e-12; // ~0.000000000001
  private static final int MAX_ITERATIONS = 100;

  /**
   * Calculates the direction (bearing from true North) using Vincenty's inverse formula for
   * geodesics on an ellipsoid.
   */
  public double calculateBearing(GeoLocation point1, GeoLocation point2) {
    // Convert coordinates to radians
    double point1LatRad = Math.toRadians(point1.getLatitude());
    double point1LonRad = Math.toRadians(point1.getLongitude());
    double point2LatRad = Math.toRadians(point2.getLatitude());
    double point2LonRad = Math.toRadians(point2.getLongitude());

    // Initialize variables for Vincenty's algorithm
    double longitudeDifference = point2LonRad - point1LonRad;
    double reducedLatitude1 = Math.atan((1 - FLATTENING) * Math.tan(point1LatRad));
    double reducedLatitude2 = Math.atan((1 - FLATTENING) * Math.tan(point2LatRad));

    double sinReducedLat1 = Math.sin(reducedLatitude1);
    double cosReducedLat1 = Math.cos(reducedLatitude1);
    double sinReducedLat2 = Math.sin(reducedLatitude2);
    double cosReducedLat2 = Math.cos(reducedLatitude2);

    // Iterative calculation of the geodesic
    double lambda = longitudeDifference;
    double previousLambda;
    int iterationCount = 0;
    double sinSigma, cosSigma, sigma, sinAlpha, cosSquaredAlpha, cos2SigmaM;

    do {
      double sinLambda = Math.sin(lambda);
      double cosLambda = Math.cos(lambda);

      sinSigma =
          Math.sqrt(
              Math.pow(cosReducedLat2 * sinLambda, 2)
                  + Math.pow(
                      cosReducedLat1 * sinReducedLat2 - sinReducedLat1 * cosReducedLat2 * cosLambda,
                      2));

      cosSigma = sinReducedLat1 * sinReducedLat2 + cosReducedLat1 * cosReducedLat2 * cosLambda;
      sigma = Math.atan2(sinSigma, cosSigma);

      sinAlpha = cosReducedLat1 * cosReducedLat2 * sinLambda / sinSigma;
      cosSquaredAlpha = 1 - sinAlpha * sinAlpha;

      cos2SigmaM =
          (cosSquaredAlpha != 0)
              ? cosSigma - 2 * sinReducedLat1 * sinReducedLat2 / cosSquaredAlpha
              : 0;

      double C = FLATTENING / 16 * cosSquaredAlpha * (4 + FLATTENING * (4 - 3 * cosSquaredAlpha));
      previousLambda = lambda;
      lambda =
          longitudeDifference
              + (1 - C)
                  * FLATTENING
                  * sinAlpha
                  * (sigma
                      + C
                          * sinSigma
                          * (cos2SigmaM + C * cosSigma * (-1 + 2 * Math.pow(cos2SigmaM, 2))));

      iterationCount++;

    } while (Math.abs(lambda - previousLambda) > CONVERGENCE_TOLERANCE
        && iterationCount < MAX_ITERATIONS);

    if (iterationCount >= MAX_ITERATIONS) {
      throw new ArithmeticException("Vincenty's formula failed to converge");
    }

    // Calculate the initial bearing (forward azimuth)
    double forwardAzimuth =
        Math.atan2(
            cosReducedLat2 * Math.sin(lambda),
            cosReducedLat1 * sinReducedLat2 - sinReducedLat1 * cosReducedLat2 * Math.cos(lambda));

    if (Double.isNaN(forwardAzimuth)) {
      return 0;
    }

    // Convert to degrees and normalize to 0-360
    return (Math.toDegrees(forwardAzimuth) + 360) % 360;
  }
}
