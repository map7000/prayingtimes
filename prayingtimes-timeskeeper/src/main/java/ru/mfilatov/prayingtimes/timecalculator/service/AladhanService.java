/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.mfilatov.prayingtimes.models.PrayerTimes;
import ru.mfilatov.prayingtimes.models.dto.PrayerTimesRequest;
import ru.mfilatov.prayingtimes.timecalculator.clients.AladhanRestApiClient;
import ru.mfilatov.prayingtimes.timecalculator.mapper.AladhanPrayerTimesMapper;
import ru.mfilatov.prayingtimes.timecalculator.model.aladhan.AladhanPrayerTimesRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class AladhanService {
  private final AladhanRestApiClient aladhanApiClient;
  private final AladhanPrayerTimesMapper prayerTimesMapper;

  public PrayerTimes getSimplifiedPrayerTimes(PrayerTimesRequest simpleRequest) {
    log.info("Fetching prayer times using method: {}", simpleRequest.method());

    AladhanPrayerTimesRequest fullRequest = prayerTimesMapper.toPrayerTimesRequest(simpleRequest);
    var response =
        aladhanApiClient.getPrayerTimes(
            fullRequest.date(),
            fullRequest.latitude() != null ? String.valueOf(fullRequest.latitude()) : null,
            fullRequest.longitude() != null ? String.valueOf(fullRequest.longitude()) : null,
            fullRequest.method(),
            fullRequest.shafaq() != null ? fullRequest.shafaq().name() : null,
            fullRequest.tune(),
            fullRequest.school(),
            fullRequest.midnightMode(),
            fullRequest.timezonestring(),
            fullRequest.latitudeAdjustmentMethod(),
            fullRequest.calendarMethod() != null ? fullRequest.calendarMethod().name() : null,
            fullRequest.iso8601());

    return prayerTimesMapper.toPrayerTimes(response);
  }
}
