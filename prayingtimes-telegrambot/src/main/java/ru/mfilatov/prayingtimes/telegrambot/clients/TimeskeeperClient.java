/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.telegrambot.clients;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mfilatov.prayingtimes.models.PrayerTimes;
import ru.mfilatov.prayingtimes.models.PrayerTimesCalculationMethod;

@FeignClient(
    value = "timeskeeperClient",
    url = "${api.timeskeeper.url:localhost:8080/api/prayer-times}")
public interface TimeskeeperClient {
  @GetMapping(path = "/getTimes")
  ResponseEntity<PrayerTimes> getTimes(
      @RequestParam(value = "date") @Valid String date,
      @RequestParam(value = "latitude") @Valid Double latitude,
      @RequestParam(value = "longitude") @Valid Double longitude,
      @RequestParam(value = "method") PrayerTimesCalculationMethod method,
      @RequestParam(value = "timezone") String timezone);
}
