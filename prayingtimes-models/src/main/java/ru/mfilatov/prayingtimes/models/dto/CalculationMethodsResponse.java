/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.models.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty; 

public record CalculationMethodsResponse(
    List<CalculationMethod> methods,
    @JsonProperty("default") String defaultMethod,
    Map<String, List<String>> categories,
    @JsonProperty("usage_note") String usageNote) {}
