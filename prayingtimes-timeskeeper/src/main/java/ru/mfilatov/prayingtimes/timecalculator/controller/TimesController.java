/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mfilatov.prayingtimes.models.PrayerTimesCalculationMethod;
import ru.mfilatov.prayingtimes.models.dto.PrayerTimesResponse;
import ru.mfilatov.prayingtimes.models.dto.RequestContext;
import ru.mfilatov.prayingtimes.timecalculator.clients.PrayerTimesClient;
import ru.mfilatov.prayingtimes.timecalculator.service.TimezoneService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prayer-times")
public class TimesController {
  private final PrayerTimesClient prayerTimesClient;
  private final TimezoneService timezoneService;

  @GetMapping("/getTimes")
  ResponseEntity<PrayerTimesResponse> getTimesByLocation(
      @RequestParam(value = "date") @DateTimeFormat(iso = ISO.DATE) LocalDate date,
      @RequestParam(value = "latitude") Double latitude,
      @RequestParam(value = "longitude") Double longitude,
      @RequestParam(value = "method", defaultValue = "MWL") PrayerTimesCalculationMethod method,
      @RequestParam(defaultValue = "UTC", value = "timezone") String timezone) {
    var providerResponse =
        prayerTimesClient.getPrayerTimes(
            latitude,
            longitude,
            date.toString(),
            method.providerName(),
            timezoneService.getOffsetHours(timezone, date));

    if (providerResponse == null || providerResponse.data() == null) {
      throw new IllegalStateException("Prayer times provider returned an empty response");
    }

    // Expose the caller's own method/timezone instead of the provider's internal values.
    var response =
        new PrayerTimesResponse(
            providerResponse.status(),
            providerResponse.data(),
            new RequestContext(
                date.toString(), new double[] {latitude, longitude}, method.name(), timezone));

    return ResponseEntity.ok(response);
  }

  @GetMapping("/getMethods")
  public ResponseEntity<PrayerTimesCalculationMethod[]> getAvailableMethods() {
    return ResponseEntity.ok(PrayerTimesCalculationMethod.values());
  }
}
