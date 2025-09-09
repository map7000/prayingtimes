/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator;

import java.time.LocalDate;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.models.PrayerTimes;

public interface PrayerCalculationStrategy {
  PrayerTimes calculatePrayerTimes(LocalDate date, GeoLocation location);
}
