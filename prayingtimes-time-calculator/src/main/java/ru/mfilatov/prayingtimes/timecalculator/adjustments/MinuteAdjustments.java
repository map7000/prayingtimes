/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.adjustments;

import java.time.LocalTime;
import ru.mfilatov.prayingtimes.models.PrayerTimes;
import ru.mfilatov.prayingtimes.timecalculator.models.CalculationParameters;

public class MinuteAdjustments {

  public static void applyAdjustments(PrayerTimes times, CalculationParameters parameters) {
    times.setFajr(adjustTime(times.getFajr(), parameters.getFajrAdjustment()));
    times.setSunrise(adjustTime(times.getSunrise(), parameters.getSunriseAdjustment()));
    times.setDhuhr(adjustTime(times.getDhuhr(), parameters.getDhuhrAdjustment()));
    times.setAsr(adjustTime(times.getAsr(), parameters.getAsrAdjustment()));
    times.setMaghrib(adjustTime(times.getMaghrib(), parameters.getMaghribAdjustment()));
    times.setIsha(adjustTime(times.getIsha(), parameters.getIshaAdjustment()));
  }

  private static LocalTime adjustTime(LocalTime time, int minutes) {
    return time != null ? time.plusMinutes(minutes) : null;
  }
}
