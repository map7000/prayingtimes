/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.models.dto;

public record PrayerTimesResponse(String status, FormattedTimes data, RequestContext request) {}
