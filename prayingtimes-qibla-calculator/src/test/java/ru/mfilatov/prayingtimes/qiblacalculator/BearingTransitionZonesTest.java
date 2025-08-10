/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static ru.mfilatov.prayingtimes.models.Constants.KAABA_LOCATION;

import org.junit.jupiter.api.Test;
import ru.mfilatov.prayingtimes.models.GeoLocation;

public class BearingTransitionZonesTest {

  private static final double PRECISION = 1.0;

  @Test
  public void testNorthAmericaTransitionZone() {
    // Test locations near the "Qibla flip" line in North America
    // where direction changes from Northeast to Southeast

    // West of transition line (should face northeast)
    GeoLocation seattle = new GeoLocation(47.6062, -122.3321, 0, "America/Los_Angeles");
    assertThat(calculateBearing(seattle, KAABA_LOCATION)).isCloseTo(18.0, within(2.0));

    // East of transition line (should face southeast)
    GeoLocation chicago = new GeoLocation(41.8781, -87.6298, 0, "America/Chicago");
    assertThat(calculateBearing(chicago, KAABA_LOCATION)).isCloseTo(52.0, within(2.0));

    // Very close locations straddling the transition line
    GeoLocation pointA = new GeoLocation(39.0, -96.0, 0, "America/Chicago");
    GeoLocation pointB = new GeoLocation(39.0, -97.0, 0, "America/Chicago");

    double bearingA = calculateBearing(pointA, KAABA_LOCATION);
    double bearingB = calculateBearing(pointB, KAABA_LOCATION);

    assertThat(Math.abs(bearingA - bearingB))
        .as("Qibla direction difference across 1 degree longitude near transition")
        .isGreaterThan(100.0);
  }

  @Test
  public void testPacificOceanTransition() {
    // Test locations where bearing changes dramatically across the Pacific

    // West of transition (facing northwest)
    GeoLocation honolulu = new GeoLocation(21.3069, -157.8583, 0, "Pacific/Honolulu");
    assertThat(calculateBearing(honolulu, KAABA_LOCATION)).isCloseTo(344.0, within(2.0));

    // East of transition (facing northeast)
    GeoLocation laPaz = new GeoLocation(-16.4897, -68.1193, 0, "America/La_Paz");
    assertThat(calculateBearing(laPaz, KAABA_LOCATION)).isCloseTo(65.0, within(2.0));

    // Small distance, big bearing change
    GeoLocation point1 = new GeoLocation(0.0, -150.0, 0, "Pacific/Tahiti");
    GeoLocation point2 = new GeoLocation(0.0, -160.0, 0, "Pacific/Tahiti");

    assertThat(
            Math.abs(
                calculateBearing(point1, KAABA_LOCATION)
                    - calculateBearing(point2, KAABA_LOCATION)))
        .as("Bearing difference across 10 degrees longitude in Pacific")
        .isGreaterThan(50.0);
  }

  @Test
  public void testAntipodalRegion() {
    // Near the antipodal point (opposite side of Earth from KAABA_LOCATION)
    // where small movements create huge bearing changes

    GeoLocation basePoint = new GeoLocation(-21.4225, -140.1738, 0, "Pacific/Tahiti");
    GeoLocation offsetPoint =
        new GeoLocation(-21.4225, -140.2738, 0, "Pacific/Tahiti"); // 0.1° west

    double baseBearing = calculateBearing(basePoint, KAABA_LOCATION);
    double offsetBearing = calculateBearing(offsetPoint, KAABA_LOCATION);

    // Bearing should change dramatically with small position changes
    assertThat(Math.abs(baseBearing - offsetBearing))
        .as("Bearing change near antipodal point")
        .isGreaterThan(45.0);
  }

  @Test
  public void testSoutheastAsiaTransition() {
    // Test locations near the transition in Southeast Asia
    // where mosques on different sides of a street might face different directions

    GeoLocation point1 = new GeoLocation(5.0, 100.0, 0, "Asia/Kuala_Lumpur");
    GeoLocation point2 = new GeoLocation(5.0, 101.0, 0, "Asia/Kuala_Lumpur");

    assertThat(
            Math.abs(
                calculateBearing(point1, KAABA_LOCATION)
                    - calculateBearing(point2, KAABA_LOCATION)))
        .as("Qibla direction difference in Malaysia transition zone")
        .isGreaterThan(30.0);
  }
}
