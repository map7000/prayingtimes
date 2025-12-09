/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mfilatov.prayingtimes.models.dto.CoordinatesResponse;
import ru.mfilatov.prayingtimes.timecalculator.clients.OpenStreetMapSearchClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

  private final OpenStreetMapSearchClient coordinatesClient;

  public CoordinatesResponse getCoordinates(String country, String city) {
    var responses = coordinatesClient.getCityLocation(city, country, "jsonv2");

    if (responses == null || responses.isEmpty()) {
      log.error("No coordinates found for: {}/{}", city, country);
      return null;
    }

    var response = responses.getFirst();

    return new CoordinatesResponse(response.lat(), response.lon());
  }
}
