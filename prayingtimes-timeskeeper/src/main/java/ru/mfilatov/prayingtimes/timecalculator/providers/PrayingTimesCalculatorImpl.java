/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.providers;

import java.time.LocalDate;
import java.util.TimeZone;
import org.springframework.stereotype.Component;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.models.PrayerTimes;
import ru.mfilatov.prayingtimes.timecalculator.PrayerTimesCalculator;
import ru.mfilatov.prayingtimes.timecalculator.models.CalculationParameters;

@Component
public class PrayingTimesCalculatorImpl {
  public PrayerTimes calculate(
      GeoLocation coordinates,
      LocalDate time,
      TimeZone timeZone,
      CalculationParameters.CalculationMethod method) {
    CalculationParameters parameters = CalculationParameters.getDefault(method);
    PrayerTimesCalculator calculator = new PrayerTimesCalculator(parameters);

    return calculator.calculate(
        time,
        new GeoLocation(
            coordinates.getLatitude(), coordinates.getLongitude(), 0, timeZone.getID()));
  }
}
