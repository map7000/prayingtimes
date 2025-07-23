/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.models;

import lombok.Data;

@Data
public class GeoLocation {
  private double latitude;
  private double longitude;
  private double elevation; // in meters
  private String timezone; // e.g., "Europe/Istanbul"

  public GeoLocation(double latitude, double longitude) {
    this(latitude, longitude, 0, "UTC");
  }

  public GeoLocation(double latitude, double longitude, double elevation, String timezone) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.elevation = elevation;
    this.timezone = timezone;
  }
}
