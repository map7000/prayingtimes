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
import ru.mfilatov.prayingtimes.models.dto.TimezoneResponse;

@FeignClient(value = "locationClient", url = "${api.location.url:localhost:8080/api/location}")
public interface LocationClient {
  @GetMapping(path = "/timezone")
  ResponseEntity<TimezoneResponse> getTimes(
      @RequestParam(value = "latitude") @Valid Double latitude,
      @RequestParam(value = "longitude") @Valid Double longitude);
}
