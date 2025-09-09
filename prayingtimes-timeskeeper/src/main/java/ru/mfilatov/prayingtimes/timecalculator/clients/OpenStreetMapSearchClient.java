/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.clients;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mfilatov.prayingtimes.timecalculator.model.openstreetmap.SearchJsonV2;

@FeignClient(
    value = "openStreetMapSearchClient",
    url = "${location.api.base-url:https://nominatim.openstreetmap.org/}")
public interface OpenStreetMapSearchClient {

  @GetMapping(path = "search")
  List<SearchJsonV2> getCityLocation(
      @RequestParam(value = "city") String city,
      @RequestParam(value = "country") String country,
      @RequestParam(value = "format") String format);
}
