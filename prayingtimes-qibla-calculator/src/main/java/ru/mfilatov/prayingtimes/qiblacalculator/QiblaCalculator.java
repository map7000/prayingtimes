/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator;

import java.time.LocalDate;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.qiblacalculator.adapters.DistanceToKaabaAdapter;
import ru.mfilatov.prayingtimes.qiblacalculator.adapters.MagneticDeclinationAdapter;
import ru.mfilatov.prayingtimes.qiblacalculator.adapters.TrueDirectionAdapter;

public class QiblaCalculator {

  /**
   * Calculate true Qibla direction (astronomical north)
   *
   * @param location Geographic coordinates
   * @return Bearing in degrees from North (0° to 360°)
   */
  public double calculateTrueDirection(GeoLocation location) {
    return TrueDirectionAdapter.calculateBearing(
        location, TrueDirectionAdapter.CalculationMethod.VINCENTY);
  }

  /**
   * Calculate magnetic Qibla direction (compass north)
   *
   * @param location Geographic coordinates
   * @param date Calculation date (for magnetic declination)
   * @return Corrected bearing in degrees from magnetic North
   */
  public double calculateMagneticDirection(GeoLocation location, LocalDate date) {
    double trueBearing = calculateTrueDirection(location);

    double declination =
        MagneticDeclinationAdapter.getDeclination(
            location, date, MagneticDeclinationAdapter.CalculationMethod.WORLD_MAGNETIC_MODEL);
    return (trueBearing + declination + 360) % 360;
  }

  /**
   * Calculate great-circle distance to Kaaba
   *
   * @param location Geographic coordinates
   * @return Distance in kilometers
   */
  public double calculateDistanceToKaaba(GeoLocation location) {
    return DistanceToKaabaAdapter.calculateDistance(
        location, DistanceToKaabaAdapter.DistanceMethod.VINCENTY);
  }
}
