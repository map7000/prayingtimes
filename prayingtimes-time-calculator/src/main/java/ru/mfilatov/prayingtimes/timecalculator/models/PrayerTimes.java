/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.models;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrayerTimes {
  private LocalTime fajr;
  private LocalTime sunrise;
  private LocalTime dhuhr;
  private LocalTime asr;
  private LocalTime maghrib;
  private LocalTime sunset;
  private LocalTime isha;
}
