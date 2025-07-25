/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.distance;

import ru.mfilatov.prayingtimes.models.GeoLocation;

/**
 * High-precision distance calculation using Vincenty's inverse formula Reference: "Direct and
 * Inverse Solutions of Geodesics on the Ellipsoid" (Vincenty, 1975)
 *
 * <p>Accuracy: ±0.5mm on WGS84 ellipsoid Typical use: Surveying, precise Qibla direction
 * calculations
 */
public class VincentyDistanceCalculator {

  // WGS84 ellipsoid parameters
  private static final double SEMI_MAJOR_AXIS = 6_378_137.0; // a (meters)
  private static final double FLATTENING = 1 / 298.257223563; // f
  private static final double SEMI_MINOR_AXIS = SEMI_MAJOR_AXIS * (1 - FLATTENING); // b
  private static final double TOLERANCE = 1e-12; // 0.000000000001
  private static final int MAX_ITERATIONS = 200;

  /**
   * Calculate distance between two points using Vincenty inverse formula
   *
   * @param point1 First geographic point
   * @param point2 Second geographic point
   * @return Distance in meters between points
   * @throws ArithmeticException if formula fails to converge
   */
  public static double calculateDistance(GeoLocation point1, GeoLocation point2) {
    // Convert coordinates to radians
    double lat1 = Math.toRadians(point1.getLatitude());
    double lon1 = Math.toRadians(point1.getLongitude());
    double lat2 = Math.toRadians(point2.getLatitude());
    double lon2 = Math.toRadians(point2.getLongitude());

    // Reduced latitudes (parametric latitude)
    double U1 = Math.atan((1 - FLATTENING) * Math.tan(lat1));
    double U2 = Math.atan((1 - FLATTENING) * Math.tan(lat2));

    double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
    double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

    // Longitudinal difference
    double L = lon2 - lon1;
    double lambda = L, previousLambda;
    int iterations = 0;

    // Vincenty iterative calculation
    double sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
    do {
      double sinLambda = Math.sin(lambda), cosLambda = Math.cos(lambda);

      sinSigma =
          Math.sqrt(
              Math.pow(cosU2 * sinLambda, 2)
                  + Math.pow(cosU1 * sinU2 - sinU1 * cosU2 * cosLambda, 2));

      cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
      sigma = Math.atan2(sinSigma, cosSigma);

      sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
      cosSqAlpha = 1 - sinAlpha * sinAlpha;

      cos2SigmaM = (cosSqAlpha != 0) ? cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha : 0;

      double C = FLATTENING / 16 * cosSqAlpha * (4 + FLATTENING * (4 - 3 * cosSqAlpha));

      previousLambda = lambda;
      lambda =
          L
              + (1 - C)
                  * FLATTENING
                  * sinAlpha
                  * (sigma
                      + C
                          * sinSigma
                          * (cos2SigmaM + C * cosSigma * (-1 + 2 * Math.pow(cos2SigmaM, 2))));

    } while (Math.abs(lambda - previousLambda) > TOLERANCE && ++iterations < MAX_ITERATIONS);

    if (iterations >= MAX_ITERATIONS) {
      throw new ArithmeticException("Vincenty formula failed to converge");
    }

    // Final distance calculation
    double uSq =
        cosSqAlpha
            * (Math.pow(SEMI_MAJOR_AXIS, 2) - Math.pow(SEMI_MINOR_AXIS, 2))
            / Math.pow(SEMI_MINOR_AXIS, 2);

    double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));

    double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));

    double deltaSigma =
        B
            * sinSigma
            * (cos2SigmaM
                + B
                    / 4
                    * (cosSigma * (-1 + 2 * Math.pow(cos2SigmaM, 2))
                        - B
                            / 6
                            * cos2SigmaM
                            * (-3 + 4 * Math.pow(sinSigma, 2))
                            * (-3 + 4 * Math.pow(cos2SigmaM, 2))));

    return SEMI_MINOR_AXIS * A * (sigma - deltaSigma);
  }

  /** Check if points are antipodal (opposite sides of Earth) */
  public static boolean isAntipodal(GeoLocation p1, GeoLocation p2) {
    return Math.abs(p1.getLatitude() + p2.getLatitude()) < 1e-6
        && Math.abs(p1.getLongitude() - p2.getLongitude() - 180) < 1e-6;
  }
}
