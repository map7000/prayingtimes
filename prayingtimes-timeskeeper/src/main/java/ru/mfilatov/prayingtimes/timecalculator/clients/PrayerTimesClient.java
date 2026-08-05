/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.clients;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.mfilatov.prayingtimes.models.dto.*;

@Service
public class PrayerTimesClient {

  private final RestClient restClient;

  public PrayerTimesClient(RestClient prayerTimesRestClient) {
    this.restClient = prayerTimesRestClient;
  }

  /**
   * Get prayer times for a specific date.
   *
   * @param lat Latitude (Required)
   * @param lng Longitude (Required)
   * @param date Date in YYYY-MM-DD format (Optional, defaults to today)
   * @param method Calculation method name (Optional, e.g., "isna", "mwl")
   * @param timezone Timezone string (Optional, e.g., "America/New_York")
   */
  public PrayerTimesResponse getPrayerTimes(
      double lat, double lng, String date, String method, String timezone) {
    return restClient
        .get()
        .uri(
            uriBuilder -> {
              uriBuilder.path("/api/prayer-times").queryParam("lat", lat).queryParam("lng", lng);

              // Conditionally add optional parameters
              if (date != null) uriBuilder.queryParam("date", date);
              if (method != null) uriBuilder.queryParam("method", method);
              if (timezone != null) uriBuilder.queryParam("timezone", timezone);

              return uriBuilder.build();
            })
        .retrieve()
        .body(PrayerTimesResponse.class);
  }

  /**
   * Get prayer times for a range of dates.
   *
   * @param lat Latitude (Required)
   * @param lng Longitude (Required)
   * @param startDate Start date in YYYY-MM-DD format (Required)
   * @param endDate End date in YYYY-MM-DD format (Required)
   * @param method Calculation method name (Optional)
   * @param timezone Timezone string (Optional)
   */
  public PrayerTimesRangeResponse getPrayerTimesRange(
      double lat, double lng, String startDate, String endDate, String method, String timezone) {
    return restClient
        .get()
        .uri(
            uriBuilder -> {
              uriBuilder
                  .path("/api/prayer-times/range")
                  .queryParam("lat", lat)
                  .queryParam("lng", lng)
                  .queryParam("start_date", startDate)
                  .queryParam("end_date", endDate);

              if (method != null) uriBuilder.queryParam("method", method);
              if (timezone != null) uriBuilder.queryParam("timezone", timezone);

              return uriBuilder.build();
            })
        .retrieve()
        .body(PrayerTimesRangeResponse.class);
  }

  /** Get the list of supported calculation methods. */
  public CalculationMethodsResponse getMethods() {
    return restClient.get().uri("/api/methods").retrieve().body(CalculationMethodsResponse.class);
  }
}
