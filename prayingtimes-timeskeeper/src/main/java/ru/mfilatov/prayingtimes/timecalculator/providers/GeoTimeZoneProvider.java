/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.providers;

import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.timecalculator.clients.GeoTimeZoneClient;

@Service
public class GeoTimeZoneProvider implements TimeZoneProvider {
  private final GeoTimeZoneClient client;

  @Autowired
  public GeoTimeZoneProvider(GeoTimeZoneClient client) {
    this.client = client;
  }

  @Override
  public TimeZone getTimeZone(GeoLocation coordinates) {
    var rs = client.getTimeZone(coordinates.getLatitude(), coordinates.getLongitude());
    return TimeZone.getTimeZone(ZoneId.of(rs.ianaTimezone()));
  }
}
