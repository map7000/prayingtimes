/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record CalculationMethod(
    String name,
    @JsonProperty("display_name") String displayName,
    String description,
    Map<String, Object> params // Params contain mixed types (Double, String)
    ) {}
