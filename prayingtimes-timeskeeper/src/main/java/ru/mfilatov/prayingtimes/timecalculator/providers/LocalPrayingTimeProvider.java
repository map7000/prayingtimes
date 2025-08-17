/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.providers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.models.PrayerTimes;
import ru.mfilatov.prayingtimes.timecalculator.models.CalculationParameters;

@Service
public class LocalPrayingTimeProvider implements PrayingTimesProvider {

  private final PrayingTimesCalculatorImpl calculator;

  private static final DateTimeFormatter formater = DateTimeFormatter.ofPattern("HH:mm");

  @Autowired
  LocalPrayingTimeProvider(PrayingTimesCalculatorImpl calculator) {
    this.calculator = calculator;
  }

  @Override
  public PrayerTimes getTimesByCoordinates(
      GeoLocation coordinates, TimeZone timeZone, CalculationParameters.CalculationMethod method) {
    return calculator.calculate(coordinates, LocalDate.now(), timeZone, method);
  }

  private String formatTimezone(Integer timezone) {
    String result;
    if (timezone > 0) {
      result = "+" + timezone;
    } else if (timezone < 0) {
      result = timezone.toString();
    } else {
      result = "UTC";
    }
    return result;
  }
}
