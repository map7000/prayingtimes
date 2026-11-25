/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import ru.mfilatov.prayingtimes.models.PrayerTimesCalculationMethod;
import ru.mfilatov.prayingtimes.models.dto.FormattedTimes;
import ru.mfilatov.prayingtimes.models.dto.PrayerTimesResponse;
import ru.mfilatov.prayingtimes.models.dto.RequestContext;
import ru.mfilatov.prayingtimes.timecalculator.clients.PrayerTimesClient;
import ru.mfilatov.prayingtimes.timecalculator.service.TimezoneService;

class TimesControllerTest {

  private final PrayerTimesClient client = mock(PrayerTimesClient.class);
  private final TimezoneService timezoneService = mock(TimezoneService.class);
  private final TimesController controller = new TimesController(client, timezoneService);

  @Test
  void translatesPublicRequestToProviderAndBack() {
    var date = LocalDate.of(2026, 11, 25);
    var times =
        new FormattedTimes(
            "06:09", "06:09", "08:24", "12:16", "14:20", "16:08", "16:08", "18:16", "00:16");
    var providerResponse =
        new PrayerTimesResponse(
            "success",
            times,
            new RequestContext("2026-11-25", new double[] {55.75, 37.62}, "makkah", "3.0"));

    when(timezoneService.getOffsetHours("Europe/Moscow", date)).thenReturn(3.0);
    when(client.getPrayerTimes(55.75, 37.62, "2026-11-25", "makkah", 3.0))
        .thenReturn(providerResponse);

    var response =
        controller.getTimesByLocation(
            date, 55.75, 37.62, PrayerTimesCalculationMethod.UMM_AL_QURA, "Europe/Moscow");

    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().data()).isEqualTo(times);
    assertThat(response.getBody().request().method()).isEqualTo("UMM_AL_QURA");
    assertThat(response.getBody().request().timezone()).isEqualTo("Europe/Moscow");
    assertThat(response.getBody().request().date()).isEqualTo("2026-11-25");
    assertThat(response.getBody().request().coordinates()).containsExactly(55.75, 37.62);

    verify(timezoneService).getOffsetHours("Europe/Moscow", date);
    verify(client).getPrayerTimes(55.75, 37.62, "2026-11-25", "makkah", 3.0);
  }

  @Test
  void rejectsAnEmptyProviderResponse() {
    var date = LocalDate.of(2026, 11, 25);
    when(timezoneService.getOffsetHours("UTC", date)).thenReturn(0.0);
    when(client.getPrayerTimes(0.0, 0.0, "2026-11-25", "mwl", 0.0)).thenReturn(null);

    org.assertj.core.api.Assertions.assertThatThrownBy(
            () ->
                controller.getTimesByLocation(
                    date, 0.0, 0.0, PrayerTimesCalculationMethod.MWL, "UTC"))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("empty response");
  }
}
