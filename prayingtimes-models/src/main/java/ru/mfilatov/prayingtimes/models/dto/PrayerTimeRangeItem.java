/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.models.dto;

public record PrayerTimeRangeItem(
    String date,
    String fajr,
    String sunrise,
    String dhuhr,
    String asr,
    String maghrib,
    String isha) {}
