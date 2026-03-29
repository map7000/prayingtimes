/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.clients;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.mfilatov.prayingtimes.timecalculator.model.openstreetmap.SearchJsonV2;

@Component
public class OpenStreetMapSearchClient {

  private final RestClient restClient;

  public OpenStreetMapSearchClient(
      RestClient.Builder restClientBuilder,
      @Value("${location.api.base-url:https://nominatim.openstreetmap.org/}") String baseUrl,
      @Value(
              "${location.api.user-agent:PrayingTimesApp/1.0 (https://github.com/map7000/prayingtimes)}")
          String userAgent) {
    this.restClient =
        restClientBuilder.baseUrl(baseUrl).defaultHeader(HttpHeaders.USER_AGENT, userAgent).build();
  }

  public List<SearchJsonV2> getCityLocation(String city, String country, String format) {
    return restClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("search")
                    .queryParam("city", city)
                    .queryParam("country", country)
                    .queryParam("format", format)
                    .build())
        .retrieve()
        .body(new ParameterizedTypeReference<>() {});
  }
}
