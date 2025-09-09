/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator;

import java.time.LocalDate;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.models.PrayerTimes;
import ru.mfilatov.prayingtimes.timecalculator.adjustments.HighLatitudeAdjuster;
import ru.mfilatov.prayingtimes.timecalculator.adjustments.MinuteAdjustments;
import ru.mfilatov.prayingtimes.timecalculator.adjustments.PolarAdjuster;
import ru.mfilatov.prayingtimes.timecalculator.models.CalculationParameters;
import ru.mfilatov.prayingtimes.timecalculator.utils.NightPortion;
import ru.mfilatov.prayingtimes.timecalculator.utils.PolarUtils;

public class PrayerTimesCalculator {
  private final CalculationParameters parameters;

  public PrayerTimesCalculator(CalculationParameters parameters) {
    this.parameters = parameters;
  }

  public PrayerTimes calculate(LocalDate date, GeoLocation location) {
    ru.mfilatov.prayingtimes.timecalculator.PrayerCalculationStrategy strategy =
        getCalculationStrategy();
    PrayerTimes times = strategy.calculatePrayerTimes(date, location);

    // Apply adjustments in this order:
    if (PolarUtils.isPolarDay(date, location.getLatitude())
        || PolarUtils.isPolarNight(date, location.getLatitude())) {
      PolarAdjuster.adjust(times, date, location, parameters);
    } else if (NightPortion.needsAdjustment(location.getLatitude())) {
      HighLatitudeAdjuster.adjust(times, date, location, parameters);
    }

    MinuteAdjustments.applyAdjustments(times, parameters);

    return times;
  }

  private PrayerCalculationStrategy getCalculationStrategy() {
    switch (parameters.getMethod()) {
      case MWL:
        return new MWLCalculationStrategy(parameters);
      default:
        throw new IllegalArgumentException("Unsupported calculation method");
    }
  }
}
