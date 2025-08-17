/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculatoor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.timecalculator.clients.GeoTimeZoneClient;
import ru.mfilatov.prayingtimes.timecalculator.providers.GeoTimeZoneProvider;

@SpringBootTest
public class GeoTimeZoneProviderTest {
  @Autowired GeoTimeZoneClient timeZoneClient;

  @Test
  void test() {
    var timeZoneProvider = new GeoTimeZoneProvider(timeZoneClient);
    var tz = timeZoneProvider.getTimeZone(new GeoLocation(55.7505412, 37.6174782));
    assertThat(tz.getDisplayName()).isEqualTo("3");
  }
}
