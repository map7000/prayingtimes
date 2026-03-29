/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.mfilatov.prayingtimes.timecalculator.model.aladhan.AladhanResponse;

@Component
public class AladhanRestApiClient {

  private final RestClient restClient;

  public AladhanRestApiClient(
      RestClient.Builder restClientBuilder,
      @Value("${aladhan.api.base-url:https://api.aladhan.com/v1/timings}") String baseUrl) {
    this.restClient = restClientBuilder.baseUrl(baseUrl).build();
  }

  public AladhanResponse getPrayerTimes(
      String date,
      String latitude,
      String longitude,
      Integer method,
      String shafaq,
      String tune,
      Integer school,
      Integer midnightMode,
      String timezonestring,
      Integer latitudeAdjustmentMethod,
      String calendarMethod,
      Boolean iso8601) {
    return restClient
        .get()
        .uri(
            uriBuilder -> {
              var builder = uriBuilder.path("/" + date);

              if (latitude != null) {
                builder.queryParam("latitude", latitude);
              }
              if (longitude != null) {
                builder.queryParam("longitude", longitude);
              }
              if (method != null) {
                builder.queryParam("method", method);
              }
              if (shafaq != null) {
                builder.queryParam("shafaq", shafaq);
              }
              if (tune != null) {
                builder.queryParam("tune", tune);
              }
              if (school != null) {
                builder.queryParam("school", school);
              }
              if (midnightMode != null) {
                builder.queryParam("midnightMode", midnightMode);
              }
              if (timezonestring != null) {
                builder.queryParam("timezonestring", timezonestring);
              }
              if (latitudeAdjustmentMethod != null) {
                builder.queryParam("latitudeAdjustmentMethod", latitudeAdjustmentMethod);
              }
              if (calendarMethod != null) {
                builder.queryParam("calendarMethod", calendarMethod);
              }
              if (iso8601 != null) {
                builder.queryParam("iso8601", iso8601);
              }

              return builder.build();
            })
        .retrieve()
        .body(AladhanResponse.class);
  }
}
