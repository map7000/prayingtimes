/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.models.dto;

public record RequestContext(
    String date,
    double[] coordinates, // Serialized as [lat, lng] in JSON
    String method,
    String timezone) {}
