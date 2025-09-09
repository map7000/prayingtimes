/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.geomagnetic;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import ru.mfilatov.prayingtimes.models.GeoLocation;

public class WorldMagneticModel {
  private static final List<double[]> coefficients;

  static {
    try {
      coefficients = WMCCoefficientLoader.loadFromResource("WMM2025.txt");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static final double EARTH_RADIUS_KM = 6371.2;
  private static final double WMM_EPOCH = 2025.0;
  private static final int MAX_DEGREE = 10;

  /**
   * Calculate magnetic declination for given location and date
   *
   * @param location Target location
   * @param date Calculation date (must be between 2025-2030)
   * @return Magnetic declination in degrees (East positive)
   */
  public static double calculateDeclination(GeoLocation location, LocalDate date) {
    // Validate input
    if (location.getLatitude() < -90
        || location.getLatitude() > 90
        || location.getLongitude() < -180
        || location.getLongitude() > 180) {
      throw new IllegalArgumentException("Invalid coordinates");
    }

    double decimalYear = getDecimalYear(date);
    if (decimalYear < 2025.0 || decimalYear > 2030.0) {
      throw new IllegalArgumentException("WMM2025 only valid for 2025-2030");
    }

    // Convert to spherical coordinates
    double phi = Math.toRadians(location.getLatitude());
    double lambda = Math.toRadians(location.getLongitude());
    double r = EARTH_RADIUS_KM + (location.getElevation() / 1000);

    // Calculate magnetic field components
    MagneticField field = calculateField(phi, lambda, r, decimalYear);

    // Declination = arctan(Y / X)
    return Math.toDegrees(Math.atan2(field.y, field.x));
  }

  private static double getDecimalYear(LocalDate date) {
    int year = date.getYear();
    int dayOfYear = date.getDayOfYear();
    boolean isLeap = date.isLeapYear();

    double fraction = dayOfYear / (isLeap ? 366.0 : 365.0);
    return year + fraction;
  }

  private static MagneticField calculateField(double phi, double lambda, double r, double t) {
    double cosPhi = Math.cos(phi);
    double sinPhi = Math.sin(phi);
    double ratio = EARTH_RADIUS_KM / r;

    // Initialize sums
    double x = 0, y = 0, z = 0;

    // Precompute cos/sin terms
    double[] cosMLambda = new double[MAX_DEGREE + 1];
    double[] sinMLambda = new double[MAX_DEGREE + 1];
    for (int m = 0; m <= MAX_DEGREE; m++) {
      cosMLambda[m] = Math.cos(m * lambda);
      sinMLambda[m] = Math.sin(m * lambda);
    }

    // Summation over all coefficients
    int index = 0;
    for (int n = 1; n <= MAX_DEGREE; n++) {
      double ratioPow = Math.pow(ratio, n + 2);

      for (int m = 0; m <= n; m++) {
        double[] cof = coefficients.get(index++);
        double g = cof[0] + (t - WMM_EPOCH) * cof[2];
        double h = cof[1] + (t - WMM_EPOCH) * cof[3];

        // Schmidt semi-normalized associated Legendre functions
        double p = calculateLegendreP(n, m, sinPhi);
        double dp = calculateLegendreDP(n, m, sinPhi, p);

        // Summation
        double common = ratioPow * (g * cosMLambda[m] + h * sinMLambda[m]);
        x += common * dp;
        y += ratioPow * m * (g * sinMLambda[m] - h * cosMLambda[m]) * p / cosPhi;
        z -= (n + 1) * common * p;
      }
    }

    return new MagneticField(x, y, z);
  }

  // Legendre polynomial calculation using recurrence relations
  private static double calculateLegendreP(int n, int m, double sinPhi) {
    if (n < m) return 0;

    // Schmidt semi-normalization factor
    double factor = (m == 0) ? 1.0 : Math.sqrt(2.0 * factorial(n - m) / factorial(n + m));

    // Associated Legendre polynomial
    double pmm = 1.0;
    if (m > 0) {
      double somx2 = Math.sqrt((1.0 - sinPhi) * (1.0 + sinPhi));
      pmm = factorial2(2 * m - 1) * Math.pow(somx2, m);
    }

    if (n == m) return factor * pmm;

    double pmmp1 = sinPhi * (2 * m + 1) * pmm;
    if (n == m + 1) return factor * pmmp1;

    double pll = 0;
    for (int l = m + 2; l <= n; l++) {
      pll = (sinPhi * (2 * l - 1) * pmmp1 - (l + m - 1) * pmm) / (l - m);
      pmm = pmmp1;
      pmmp1 = pll;
    }

    return factor * pll;
  }

  // Derivative of Legendre polynomial
  private static double calculateLegendreDP(int n, int m, double sinPhi, double p) {
    double cosPhi = Math.sqrt(1 - sinPhi * sinPhi);
    if (cosPhi < 1e-10) { // Near poles
      if (m == 1) {
        return n * (n + 1) / 2.0;
      } else if (m == -1) {
        return -p / (2.0 * cosPhi);
      } else {
        return 0;
      }
    }
    return (m * sinPhi * p - (n + m) * (n - m + 1) * calculateLegendreP(n, m - 1, sinPhi)) / cosPhi;
  }

  // Helper methods for factorial calculations
  private static double factorial(int n) {
    double result = 1;
    for (int i = 2; i <= n; i++) {
      result *= i;
    }
    return result;
  }

  private static double factorial2(int n) {
    double result = 1;
    for (int i = n; i > 0; i -= 2) {
      result *= i;
    }
    return result;
  }

  private record MagneticField(double x, double y, double z) {}
}
