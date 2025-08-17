/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.models.PrayerTimes;
import ru.mfilatov.prayingtimes.timecalculator.models.CalculationParameters;
import ru.mfilatov.prayingtimes.timecalculator.providers.CoordinatesProvider;
import ru.mfilatov.prayingtimes.timecalculator.providers.PrayingTimesProvider;
import ru.mfilatov.prayingtimes.timecalculator.providers.TimeZoneProvider;

@RestController
public class TimesRestController {
  private final CoordinatesProvider coordinatesProvider;
  private final TimeZoneProvider timeZoneProvider;
  private final PrayingTimesProvider prayingTimesProvider;

  @Autowired
  public TimesRestController(
      CoordinatesProvider coordinatesProvider,
      TimeZoneProvider timeZoneProvider,
      PrayingTimesProvider prayingTimesProvider) {
    this.coordinatesProvider = coordinatesProvider;
    this.timeZoneProvider = timeZoneProvider;
    this.prayingTimesProvider = prayingTimesProvider;
  }

  @GetMapping("/getTimesByLocation")
  PrayerTimes getTimesByLocation(
      @RequestParam("country") String country,
      @RequestParam("city") String city,
      @RequestParam("method") CalculationParameters.CalculationMethod method) {
    var coordinates = coordinatesProvider.getCoordinatesByCityName(city, country);
    var timezone = timeZoneProvider.getTimeZone(coordinates);

    return prayingTimesProvider.getTimesByCoordinates(coordinates, timezone, method);
  }

  @GetMapping("/getTimesByCoordinates")
  PrayerTimes getTimesByCoordinates(
      @RequestParam("latitude") Double latitude,
      @RequestParam("longitude") Double longitude,
      @RequestParam("method") CalculationParameters.CalculationMethod method) {
    var coordinates = new GeoLocation(latitude, longitude);
    var timezone = timeZoneProvider.getTimeZone(coordinates);

    return prayingTimesProvider.getTimesByCoordinates(coordinates, timezone, method);
  }
}
