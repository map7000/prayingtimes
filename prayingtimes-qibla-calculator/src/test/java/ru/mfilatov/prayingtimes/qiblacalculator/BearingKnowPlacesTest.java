/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;
import ru.mfilatov.prayingtimes.models.GeoLocation;

public class BearingKnowPlacesTest {

  private static final double QIBLA_PRECISION = 1.0; // 1 degree precision for Qibla tests
  private static final double PRECISION = 0.0001;
  private static final GeoLocation KAABA = new GeoLocation(21.4225, 39.8262, 0, "Asia/Riyadh");

  // Test data from established Qibla calculation sources
  private static final Object[][] KNOWN_QIBLAS = {
    // City, Lat, Lon, Expected Qibla (degrees from North)
    {"Mecca", 21.4225, 39.8262, 0.0}, // Facing the Kaaba while at Kaaba
    {"Medina", 24.4667, 39.6000, 165.0},
    {"Jerusalem", 31.7833, 35.2167, 158.0},
    {"Cairo", 30.0444, 31.2357, 136.0},
    {"Istanbul", 41.0082, 28.9784, 151.0},
    {"Kuala Lumpur", 3.1390, 101.6869, 292.0},
    {"Jakarta", -6.2088, 106.8456, 295.0},
    {"Sydney", -33.8688, 151.2093, 277.0},
    {"London", 51.5074, -0.1278, 118.0},
    {"New York", 40.7128, -74.0060, 58.0},
    {"Los Angeles", 34.0522, -118.2437, 28.0},
    {"Tokyo", 35.6762, 139.6503, 293.0},
    {"Cape Town", -33.9249, 18.4241, 90.0}
  };

  @Test
  public void testKnownQiblaDirections() {
    for (Object[] data : KNOWN_QIBLAS) {
      String city = (String) data[0];
      double lat = (Double) data[1];
      double lon = (Double) data[2];
      double expected = (Double) data[3];

      GeoLocation location = new GeoLocation(lat, lon, 0, "UTC");
      assertThat(calculateBearing(location, KAABA))
          .as("Qibla direction for %s", city)
          .isCloseTo(expected, within(QIBLA_PRECISION));
    }
  }

  @Test
  public void testSpecialCases() {
    // North Pole should face south to Kaaba
    GeoLocation northPole = new GeoLocation(90.0, 0.0, 0, "UTC");
    assertThat(calculateBearing(northPole, KAABA)).isCloseTo(180.0, within(PRECISION));

    // South Pole should face north to Kaaba
    GeoLocation southPole = new GeoLocation(-90.0, 0.0, 0, "UTC");
    assertThat(calculateBearing(southPole, KAABA)).isCloseTo(0.0, within(PRECISION));

    // Locations where bearing wraps around 360°
    GeoLocation westernAlaska = new GeoLocation(65.0, -168.0, 0, "America/Anchorage");
    assertThat(calculateBearing(westernAlaska, KAABA)).isCloseTo(340.0, within(QIBLA_PRECISION));
  }
}
