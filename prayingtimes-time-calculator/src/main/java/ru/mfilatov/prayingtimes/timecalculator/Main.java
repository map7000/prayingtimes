/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator;

import java.time.LocalDate;
import ru.mfilatov.prayingtimes.models.GeoLocation;
import ru.mfilatov.prayingtimes.models.PrayerTimes;
import ru.mfilatov.prayingtimes.timecalculator.models.CalculationParameters;

// TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
  public static void main(String[] args) {
    // Create custom configuration
    CalculationParameters params =
        CalculationParameters.builder()
            .method(CalculationParameters.CalculationMethod.MWL)
            .fajrAngle(18.0)
            .ishaAngle(17.0)
            .asrMethod(CalculationParameters.AsrJuristicMethod.HANAFI) // Use Hanafi method for Asr
            .fajrAdjustment(2) // Add 2 minutes to Fajr
            .maghribAdjustment(3) // Add 3 minutes to Maghrib
            .highLatitudeMethod(CalculationParameters.HighLatitudeMethod.ANGLE_BASED)
            .daylightSavingEnabled(true)
            .dstOffsetMinutes(60)
            .build();

    // Create calculator
    PrayerTimesCalculator calculator = new PrayerTimesCalculator(params);

    // Calculate times
    GeoLocation london = new GeoLocation(51.5074, -0.1278, 0, "Europe/London");
    PrayerTimes times = calculator.calculate(LocalDate.now(), london);

    System.out.println("Adjusted Fajr: " + times.getFajr());
  }
}
