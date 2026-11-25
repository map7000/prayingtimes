/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.mfilatov.prayingtimes.models.dto.*;

@Service
public class PrayerTimesClient {

  /** AlAdhan-compatible school code: 0 = Shafi, 1 = Hanafi. */
  private static final int HANAFI_SCHOOL = 1;

  private final RestClient restClient;

  public PrayerTimesClient(
      RestClient.Builder restClientBuilder,
      @Value("${prayer-times.api.base-url:http://localhost:3000}") String baseUrl) {
    this.restClient = restClientBuilder.baseUrl(baseUrl).build();
  }

  /**
   * Get prayer times for a specific date.
   *
   * @param lat Latitude (Required)
   * @param lng Longitude (Required)
   * @param date Date in YYYY-MM-DD format (Optional, defaults to today)
   * @param method Provider calculation method name (Optional, e.g., "isna", "mwl")
   * @param timezoneOffset UTC offset in hours for the requested date (Optional, defaults to 0)
   */
  public PrayerTimesResponse getPrayerTimes(
      double lat, double lng, String date, String method, double timezoneOffset) {
    return restClient
        .get()
        .uri(
            uriBuilder -> {
              uriBuilder
                  .path("/api/prayer-times")
                  .queryParam("lat", lat)
                  .queryParam("lng", lng)
                  .queryParam("timezone", timezoneOffset)
                  .queryParam("school", HANAFI_SCHOOL);

              if (date != null) uriBuilder.queryParam("date", date);
              if (method != null) uriBuilder.queryParam("method", method);

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
   * @param method Provider calculation method name (Optional)
   * @param timezoneOffset UTC offset in hours for the requested range (Optional, defaults to 0)
   */
  public PrayerTimesRangeResponse getPrayerTimesRange(
      double lat,
      double lng,
      String startDate,
      String endDate,
      String method,
      double timezoneOffset) {
    return restClient
        .get()
        .uri(
            uriBuilder -> {
              uriBuilder
                  .path("/api/prayer-times/range")
                  .queryParam("lat", lat)
                  .queryParam("lng", lng)
                  .queryParam("start_date", startDate)
                  .queryParam("end_date", endDate)
                  .queryParam("timezone", timezoneOffset)
                  .queryParam("school", HANAFI_SCHOOL);

              if (method != null) uriBuilder.queryParam("method", method);

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
