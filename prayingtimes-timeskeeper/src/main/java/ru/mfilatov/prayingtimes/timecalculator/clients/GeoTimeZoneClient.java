/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mfilatov.prayingtimes.timecalculator.model.geotimezone.GeoTimeZone;

@FeignClient(value = "geoTimeZoneClient", url = "${timezone.api.base-url:https://api.geotimezone.com/public/")
public interface GeoTimeZoneClient {
  @GetMapping(path = "timezone")
  GeoTimeZone getTimeZone(
      @RequestParam(value = "latitude") Double latitude,
      @RequestParam(value = "longitude") Double longitude);
}
