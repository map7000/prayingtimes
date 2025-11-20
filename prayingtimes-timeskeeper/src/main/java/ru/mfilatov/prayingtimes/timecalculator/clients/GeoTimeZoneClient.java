/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mfilatov.prayingtimes.timecalculator.model.ipgeolocation.TimeZoneResponse;

@FeignClient(
    value = "geoTimeZoneClient",
    url = "${timezone.api.base-url:https://api.ipgeolocation.io}")
public interface GeoTimeZoneClient {
    @GetMapping("/v2/timezone")
    TimeZoneResponse getTimeZone(
            @RequestParam("apiKey") String apiKey,
            @RequestParam("lat") double latitude,
            @RequestParam("long") double longitude
    );
}
