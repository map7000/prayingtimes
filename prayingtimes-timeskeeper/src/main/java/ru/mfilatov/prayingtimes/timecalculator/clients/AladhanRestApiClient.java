/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mfilatov.prayingtimes.timecalculator.model.aladhan.AladhanResponse;

@FeignClient(
    name = "aladhanApiClient",
    url = "${aladhan.api.base-url:https://api.aladhan.com/v1/timings}")
public interface AladhanRestApiClient {
  @GetMapping(path = "/{date}")
  AladhanResponse getPrayerTimes(
      @PathVariable("date") String date,
      @RequestParam(value = "latitude") String latitude,
      @RequestParam(value = "longitude") String longitude,
      @RequestParam(value = "method", required = false) Integer method,
      @RequestParam(value = "shafaq", required = false) String shafaq,
      @RequestParam(value = "tune", required = false) String tune,
      @RequestParam(value = "school", required = false) Integer school,
      @RequestParam(value = "midnightMode", required = false) Integer midnightMode,
      @RequestParam(value = "timezonestring", required = false) String timezonestring,
      @RequestParam(value = "latitudeAdjustmentMethod", required = false)
          Integer latitudeAdjustmentMethod,
      @RequestParam(value = "calendarMethod", required = false) String calendarMethod,
      @RequestParam(value = "iso8601", required = false) Boolean iso8601);
}
