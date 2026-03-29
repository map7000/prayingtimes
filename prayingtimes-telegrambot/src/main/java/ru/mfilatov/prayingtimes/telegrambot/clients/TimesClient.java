/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.telegrambot.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.mfilatov.prayingtimes.models.PrayerTimes;
import ru.mfilatov.prayingtimes.models.PrayerTimesCalculationMethod;

@Component
public class TimesClient {

  private final RestClient restClient;

  public TimesClient(
      RestClient.Builder restClientBuilder,
      @Value("${api.timeskeeper.url:localhost:8080/api/prayer-times}") String baseUrl) {
    this.restClient = restClientBuilder.baseUrl(baseUrl).build();
  }

  public ResponseEntity<PrayerTimes> getTimes(
      String date,
      Double latitude,
      Double longitude,
      PrayerTimesCalculationMethod method,
      String timezone) {
    return restClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/getTimes")
                    .queryParam("date", date)
                    .queryParam("latitude", latitude)
                    .queryParam("longitude", longitude)
                    .queryParam("method", method.name())
                    .queryParam("timezone", timezone)
                    .build())
        .retrieve()
        .toEntity(PrayerTimes.class);
  }
}
