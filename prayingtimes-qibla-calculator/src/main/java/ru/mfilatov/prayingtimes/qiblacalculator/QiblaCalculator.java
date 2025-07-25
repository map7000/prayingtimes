/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator;

import java.time.LocalDate;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.qiblacalculator.adapters.MagneticDeclinationAdapter;

public class QiblaCalculator {

  // Kaaba coordinates (21°25'21.0"N 39°49'34.2"E)
  public static final double KAABA_LAT = 21.4225;
  public static final double KAABA_LON = 39.8262;

  /**
   * Calculate true Qibla direction (astronomical north)
   *
   * @param location Geographic coordinates
   * @return Bearing in degrees from North (0° to 360°)
   */
  public double calculateTrueDirection(GeoLocation location) {
    double lat = Math.toRadians(location.getLatitude());
    double lon = Math.toRadians(location.getLongitude());
    double kaabaLat = Math.toRadians(KAABA_LAT);
    double kaabaLon = Math.toRadians(KAABA_LON);

    double y = Math.sin(kaabaLon - lon) * Math.cos(kaabaLat);
    double x =
        Math.cos(lat) * Math.sin(kaabaLat)
            - Math.sin(lat) * Math.cos(kaabaLat) * Math.cos(kaabaLon - lon);

    double bearing = Math.toDegrees(Math.atan2(y, x));
    return (bearing + 360) % 360;
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
            location.getLatitude(), location.getLongitude(), location.getElevation(), date);
    return (trueBearing + declination + 360) % 360;
  }

  /**
   * Calculate great-circle distance to Kaaba
   *
   * @param location Geographic coordinates
   * @return Distance in kilometers
   */
  public double calculateDistanceToKaaba(GeoLocation location) {
    double lat1 = Math.toRadians(location.getLatitude());
    double lon1 = Math.toRadians(location.getLongitude());
    double lat2 = Math.toRadians(KAABA_LAT);
    double lon2 = Math.toRadians(KAABA_LON);

    double dLat = lat2 - lat1;
    double dLon = lon2 - lon1;

    double a =
        Math.pow(Math.sin(dLat / 2), 2)
            + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return 6371 * c; // Earth radius in km
  }

  /**
   * Get Kaaba coordinates
   *
   * @return GeoLocation object with Kaaba's position
   */
  public static GeoLocation getKaabaLocation() {
    return new GeoLocation(KAABA_LAT, KAABA_LON, 0, "Asia/Riyadh");
  }
}
