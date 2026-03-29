/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.telegrambot.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.mfilatov.prayingtimes.models.dto.TimezoneResponse;

@Component
public class LocationClient {

  private final RestClient restClient;

  public LocationClient(
      RestClient.Builder restClientBuilder,
      @Value("${api.location.url:localhost:8080/api/location}") String baseUrl) {
    this.restClient = restClientBuilder.baseUrl(baseUrl).build();
  }

  public ResponseEntity<TimezoneResponse> getTimezone(Double latitude, Double longitude) {
    return restClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/timezone")
                    .queryParam("latitude", latitude)
                    .queryParam("longitude", longitude)
                    .build())
        .retrieve()
        .toEntity(TimezoneResponse.class);
  }
}
