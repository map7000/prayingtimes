/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.providers;

import java.util.TimeZone;
import ru.mfilatov.prayingtimes.models.GeoLocation;

public interface TimeZoneProvider {
  TimeZone getTimeZone(GeoLocation coordinates);
}
