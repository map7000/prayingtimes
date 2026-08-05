/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mfilatov.prayingtimes.models.PrayerTimesCalculationMethod;
import ru.mfilatov.prayingtimes.models.dto.PrayerTimesResponse;
import ru.mfilatov.prayingtimes.timecalculator.clients.PrayerTimesClient;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prayer-times")
public class TimesController {
  PrayerTimesClient prayerTimesClient;

  @GetMapping("/getTimes")
  ResponseEntity<PrayerTimesResponse> getTimesByLocation(
      @RequestParam(value = "date") @Valid String date,
      @RequestParam(value = "latitude") @Valid Double latitude,
      @RequestParam(value = "longitude") @Valid Double longitude,
      @RequestParam(value = "method", defaultValue = "MWL") PrayerTimesCalculationMethod method,
      @RequestParam(defaultValue = "UTC", value = "timezone") String timezone) {
    var response =
        prayerTimesClient.getPrayerTimes(latitude, longitude, date, method.name(), timezone);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/getMethods")
  public ResponseEntity<PrayerTimesCalculationMethod[]> getAvailableMethods() {
    return ResponseEntity.ok(PrayerTimesCalculationMethod.values());
  }
}
