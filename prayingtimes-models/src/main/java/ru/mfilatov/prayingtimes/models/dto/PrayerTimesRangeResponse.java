/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.models.dto;

import java.util.List;

public record PrayerTimesRangeResponse(
    String status, List<PrayerTimeRangeItem> data, RequestContext request) {}
