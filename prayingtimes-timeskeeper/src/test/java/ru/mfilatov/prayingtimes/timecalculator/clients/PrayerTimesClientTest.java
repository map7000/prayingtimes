/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.clients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class PrayerTimesClientTest {

  private static final String BASE_URL = "http://prayer-times-calculator:3000";

  private static final String SINGLE_DATE_RESPONSE =
      """
      {
        "status": "success",
        "data": {
          "imsak": "04:47",
          "fajr": "04:47",
          "sunrise": "06:02",
          "dhuhr": "12:21",
          "asr": "16:05",
          "sunset": "18:40",
          "maghrib": "18:43",
          "isha": "19:38",
          "midnight": "23:43"
        },
        "request": {
          "date": "2026-04-15",
          "coordinates": [21.4225, 39.8262],
          "method": "makkah",
          "timezone": 3.0
        }
      }
      """;

  @Test
  void sendsProviderSpecificQueryParametersAndParsesTheResponse() {
    RestClient.Builder builder = RestClient.builder();
    MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
    PrayerTimesClient client = new PrayerTimesClient(builder, BASE_URL);

    server
        .expect(
            requestTo(
                BASE_URL
                    + "/api/prayer-times?lat=21.4225&lng=39.8262&timezone=3.0&school=1"
                    + "&date=2026-04-15&method=makkah"))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(SINGLE_DATE_RESPONSE, MediaType.APPLICATION_JSON));

    var response = client.getPrayerTimes(21.4225, 39.8262, "2026-04-15", "makkah", 3.0);

    server.verify();
    assertThat(response.status()).isEqualTo("success");
    assertThat(response.data().fajr()).isEqualTo("04:47");
    assertThat(response.data().asr()).isEqualTo("16:05");
    assertThat(response.request().coordinates()).containsExactly(21.4225, 39.8262);
    assertThat(response.request().method()).isEqualTo("makkah");
    // The provider returns a numeric offset; the shared DTO stores it as a string.
    assertThat(response.request().timezone()).isEqualTo("3.0");
  }

  @Test
  void sendsTheRangeQueryWithTheSameCompatibilityParameters() {
    RestClient.Builder builder = RestClient.builder();
    MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
    PrayerTimesClient client = new PrayerTimesClient(builder, BASE_URL);

    server
        .expect(
            requestTo(
                BASE_URL
                    + "/api/prayer-times/range?lat=51.5074&lng=-0.1278&start_date=2026-06-14"
                    + "&end_date=2026-06-15&timezone=1.0&school=1&method=mwl"))
        .andRespond(
            withSuccess(
                """
                {
                  "status": "success",
                  "data": [
                    {"date": "2026-06-14", "fajr": "02:43", "sunrise": "04:43", "dhuhr": "13:00",
                     "asr": "17:23", "maghrib": "21:16", "isha": "23:05"}
                  ],
                  "request": {
                    "date": "2026-06-14 to 2026-06-15",
                    "coordinates": [51.5074, -0.1278],
                    "method": "mwl",
                    "timezone": 1.0
                  }
                }
                """,
                MediaType.APPLICATION_JSON));

    var response =
        client.getPrayerTimesRange(51.5074, -0.1278, "2026-06-14", "2026-06-15", "mwl", 1.0);

    server.verify();
    assertThat(response.data()).hasSize(1);
    assertThat(response.data().getFirst().fajr()).isEqualTo("02:43");
  }
}
