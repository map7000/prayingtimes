/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.models.dto;

public record FormattedTimes(
    String imsak,
    String fajr,
    String sunrise,
    String dhuhr,
    String asr,
    String sunset,
    String maghrib,
    String isha,
    String midnight) {}
