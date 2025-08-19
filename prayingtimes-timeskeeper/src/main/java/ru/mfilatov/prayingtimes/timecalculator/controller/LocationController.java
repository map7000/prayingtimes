/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mfilatov.prayingtimes.models.dto.CoordinatesResponse;
import ru.mfilatov.prayingtimes.models.dto.TimezoneResponse;
import ru.mfilatov.prayingtimes.timecalculator.service.LocationService;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {
  private final LocationService locationService;

  @GetMapping("/coordinates")
  public ResponseEntity<CoordinatesResponse> getCoordinates(
      @RequestParam(value = "country") String country, @RequestParam(value = "city") String city) {

    validateLocationParameters(country, city);

    CoordinatesResponse response = locationService.getCoordinates(country, city);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/timezone")
  public ResponseEntity<TimezoneResponse> getTimezone(
      @RequestParam(value = "latitude") double latitude,
      @RequestParam(value = "longitude") double longitude) {

    validateCoordinates(latitude, longitude);

    TimezoneResponse response = locationService.getTimezone(latitude, longitude);
    return ResponseEntity.ok(response);
  }

  private void validateLocationParameters(String country, String city) {
    if ((country == null || country.isBlank()) && (city == null || city.isBlank())) {
      throw new IllegalArgumentException("At least one of country, city must be provided");
    }
  }

  private void validateCoordinates(double latitude, double longitude) {
    if (latitude < -90 || latitude > 90) {
      throw new IllegalArgumentException("Latitude must be between -90 and 90");
    }
    if (longitude < -180 || longitude > 180) {
      throw new IllegalArgumentException("Longitude must be between -180 and 180");
    }
  }
}
