/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.qiblacalculator.direction.BearingCalculator;
import ru.mfilatov.prayingtimes.qiblacalculator.direction.GreatCircleCalculator;
import ru.mfilatov.prayingtimes.qiblacalculator.direction.PlanarApproximationCalculator;
import ru.mfilatov.prayingtimes.qiblacalculator.direction.VincentyCalculator;

public class BearingDifferentPlacesTest {
  private static final double PRECISION = 0.0001;

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testSameLocationShouldReturnZero(
      String calculatorName, BearingCalculator calculator) {
    GeoLocation start = new GeoLocation(40.7128, -74.0060, 0, "America/New_York");
    GeoLocation end = new GeoLocation(40.7128, -74.0060, 0, "America/New_York");
    assertThat(calculator.calculateBearing(start, end)).isCloseTo(0.0, within(PRECISION));
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testNorthBearing(String calculatorName, BearingCalculator calculator) {
    GeoLocation start = new GeoLocation(40.0, -74.0, 0, "America/New_York");
    GeoLocation end = new GeoLocation(41.0, -74.0, 0, "America/New_York");
    assertThat(calculator.calculateBearing(start, end)).isCloseTo(0.0, within(PRECISION));
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testEastBearing(String calculatorName, BearingCalculator calculator) {
    GeoLocation start = new GeoLocation(40.0, -74.0, 0, "America/New_York");
    GeoLocation end = new GeoLocation(40.0, -73.0, 0, "America/New_York");
    assertThat(calculator.calculateBearing(start, end)).isCloseTo(90.0, within(PRECISION));
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testSouthBearing(String calculatorName, BearingCalculator calculator) {
    GeoLocation start = new GeoLocation(40.0, -74.0, 0, "America/New_York");
    GeoLocation end = new GeoLocation(39.0, -74.0, 0, "America/New_York");
    assertThat(calculator.calculateBearing(start, end)).isCloseTo(180.0, within(PRECISION));
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testWestBearing(String calculatorName, BearingCalculator calculator) {
    GeoLocation start = new GeoLocation(40.0, -74.0, 0, "America/New_York");
    GeoLocation end = new GeoLocation(40.0, -75.0, 0, "America/New_York");
    assertThat(calculator.calculateBearing(start, end)).isCloseTo(270.0, within(PRECISION));
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testNortheastBearing(String calculatorName, BearingCalculator calculator) {
    GeoLocation start = new GeoLocation(40.0, -74.0, 0, "America/New_York");
    GeoLocation end = new GeoLocation(40.5, -73.5, 0, "America/New_York");
    assertThat(calculator.calculateBearing(start, end))
        .isStrictlyBetween(0.0, 90.0)
        .isNotCloseTo(0.0, within(1.0))
        .isNotCloseTo(90.0, within(1.0));
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testSoutheastBearing(String calculatorName, BearingCalculator calculator) {
    GeoLocation start = new GeoLocation(40.0, -74.0, 0, "America/New_York");
    GeoLocation end = new GeoLocation(39.5, -73.5, 0, "America/New_York");
    assertThat(calculator.calculateBearing(start, end))
        .isStrictlyBetween(90.0, 180.0)
        .isNotCloseTo(90.0, within(1.0))
        .isNotCloseTo(180.0, within(1.0));
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testSouthwestBearing(String calculatorName, BearingCalculator calculator) {
    GeoLocation start = new GeoLocation(40.0, -74.0, 0, "America/New_York");
    GeoLocation end = new GeoLocation(39.5, -74.5, 0, "America/New_York");
    assertThat(calculator.calculateBearing(start, end))
        .isStrictlyBetween(180.0, 270.0)
        .isNotCloseTo(180.0, within(1.0))
        .isNotCloseTo(270.0, within(1.0));
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testNorthwestBearing(String calculatorName, BearingCalculator calculator) {
    GeoLocation start = new GeoLocation(40.0, -74.0, 0, "America/New_York");
    GeoLocation end = new GeoLocation(40.5, -74.5, 0, "America/New_York");
    assertThat(calculator.calculateBearing(start, end))
        .isStrictlyBetween(270.0, 360.0)
        .isNotCloseTo(270.0, within(1.0))
        .isNotCloseTo(360.0, within(1.0));
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testCrossingPrimeMeridian(String calculatorName, BearingCalculator calculator) {
    GeoLocation start = new GeoLocation(51.5, -0.1, 0, "Europe/London");
    GeoLocation end = new GeoLocation(51.5, 0.1, 0, "Europe/London");
    assertThat(calculator.calculateBearing(start, end)).isStrictlyBetween(80.0, 100.0);
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testCrossingAntimeridian(String calculatorName, BearingCalculator calculator) {
    GeoLocation start = new GeoLocation(0.0, 179.9, 0, "Pacific/Fiji");
    GeoLocation end = new GeoLocation(0.0, -179.9, 0, "Pacific/Fiji");
    assertThat(calculator.calculateBearing(start, end)).isStrictlyBetween(80.0, 100.0);
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testNorthPoleDestination(String calculatorName, BearingCalculator calculator) {
    GeoLocation northPole = new GeoLocation(90.0, 0.0, 0, "Arctic/Longyearbyen");

    assertThat(
            calculator.calculateBearing(
                new GeoLocation(40.7128, -74.0060, 0, "America/New_York"), northPole))
        .isCloseTo(0.0, within(PRECISION));

    assertThat(
            calculator.calculateBearing(
                new GeoLocation(51.5074, -0.1278, 0, "Europe/London"), northPole))
        .isCloseTo(0.0, within(PRECISION));
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testSouthPoleDestination(String calculatorName, BearingCalculator calculator) {
    GeoLocation southPole = new GeoLocation(-90.0, 0.0, 0, "Antarctica/South_Pole");

    assertThat(
            calculator.calculateBearing(
                new GeoLocation(-33.8688, 151.2093, 0, "Australia/Sydney"), southPole))
        .isCloseTo(180.0, within(PRECISION));
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testElevationDoesNotAffectBearing(
      String calculatorName, BearingCalculator calculator) {
    GeoLocation end = new GeoLocation(41.0, -73.0, 0, "America/New_York");

    assertThat(
            calculator.calculateBearing(new GeoLocation(40.0, -74.0, 0, "America/New_York"), end))
        .isCloseTo(
            calculator.calculateBearing(
                new GeoLocation(40.0, -74.0, 1000, "America/New_York"), end),
            within(PRECISION));
  }

  @ParameterizedTest
  @MethodSource("bearingCalculators")
  public void testTimezoneDoesNotAffectBearing(
      String calculatorName, BearingCalculator calculator) {
    GeoLocation end = new GeoLocation(41.0, -73.0, 0, "America/New_York");

    assertThat(
            calculator.calculateBearing(new GeoLocation(40.0, -74.0, 0, "America/New_York"), end))
        .isCloseTo(
            calculator.calculateBearing(new GeoLocation(40.0, -74.0, 0, "UTC"), end),
            within(PRECISION));
  }

  static Stream<Arguments> bearingCalculators() {
    return Stream.of(
        Arguments.of("GreatCircle", new GreatCircleCalculator()),
        Arguments.of("PlanarApproximation", new PlanarApproximationCalculator()),
        Arguments.of("Vincenty", new VincentyCalculator()));
  }
}
