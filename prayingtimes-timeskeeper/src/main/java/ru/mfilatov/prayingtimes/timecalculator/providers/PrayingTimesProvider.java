/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.providers;

import java.util.TimeZone;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.models.PrayerTimes;
import ru.mfilatov.prayingtimes.timecalculator.models.CalculationParameters;

public interface PrayingTimesProvider {
  PrayerTimes getTimesByCoordinates(
      GeoLocation coordinates, TimeZone timeZone, CalculationParameters.CalculationMethod method);
}
