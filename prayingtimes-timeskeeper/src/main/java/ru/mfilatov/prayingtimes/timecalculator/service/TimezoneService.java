/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.iakovlev.timeshape.TimeZoneEngine;
import org.springframework.stereotype.Service;
import ru.mfilatov.prayingtimes.models.dto.TimezoneResponse;

@Service
@Slf4j
public class TimezoneService {

  private final TimeZoneEngine timeZoneEngine;

  public TimezoneService() {
    this.timeZoneEngine = TimeZoneEngine.initialize();
  }

  public TimezoneResponse getTimezone(double latitude, double longitude) {
    Optional<ZoneId> zoneId = timeZoneEngine.query(latitude, longitude);
    ZoneId targetZone = zoneId.orElse(ZoneId.of("UTC"));

    String offset = ZonedDateTime.now(targetZone).getOffset().getId();

    return new TimezoneResponse(targetZone.getId(), offset);
  }
}
