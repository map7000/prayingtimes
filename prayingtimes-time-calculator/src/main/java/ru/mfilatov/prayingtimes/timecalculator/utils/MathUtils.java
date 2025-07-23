/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.utils;

import org.apache.commons.math3.util.FastMath;

public final class MathUtils {

  private MathUtils() {} // Utility class - prevent instantiation

  /**
   * Calculates the inverse cotangent (arc cotangent) of a value
   *
   * @param x the value whose arc cotangent is to be computed
   * @return the arc cotangent of the value, in radians
   */
  public static double acot(double x) {
    if (Double.isInfinite(x)) {
      return 0.0; // acot(±∞) = 0
    }
    if (Double.isNaN(x)) {
      return Double.NaN;
    }
    return FastMath.atan(1 / x);
  }

  /**
   * More numerically stable version of acot that handles values near zero
   *
   * @param x the value whose arc cotangent is to be computed
   * @return the arc cotangent of the value, in radians
   */
  public static double acotStable(double x) {
    if (Double.isInfinite(x)) {
      return 0.0;
    }
    if (Double.isNaN(x)) {
      return Double.NaN;
    }
    // For values near zero, use atan2 for better numerical stability
    return FastMath.atan2(1.0, x);
  }

  /**
   * Calculates the inverse cotangent and returns result in degrees
   *
   * @param x the value whose arc cotangent is to be computed
   * @return the arc cotangent of the value, in degrees
   */
  public static double acotDeg(double x) {
    return FastMath.toDegrees(acot(x));
  }
}
