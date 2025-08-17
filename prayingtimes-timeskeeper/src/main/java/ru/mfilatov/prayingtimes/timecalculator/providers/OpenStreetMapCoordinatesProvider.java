/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.timecalculator.clients.OpenStreetMapSearchClient;

@Service
public class OpenStreetMapCoordinatesProvider implements CoordinatesProvider {
  private final OpenStreetMapSearchClient client;

  @Autowired
  public OpenStreetMapCoordinatesProvider(OpenStreetMapSearchClient client) {
    this.client = client;
  }

  @Override
  public GeoLocation getCoordinatesByCityName(String city, String country) {
    var response = client.getCityLocation(city, country, "jsonv2", "1").getFirst();
    return new GeoLocation(response.lat(), response.lon());
  }
}
