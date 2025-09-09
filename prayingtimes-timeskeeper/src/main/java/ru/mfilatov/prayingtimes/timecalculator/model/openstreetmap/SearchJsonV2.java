/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.model.openstreetmap;

import java.util.List;

public record SearchJsonV2(
    Long place_id,
    String licence,
    String osm_type,
    Long osm_id,
    Double lat,
    Double lon,
    String category,
    String type,
    Integer place_rank,
    Double importance,
    String addresstype,
    String name,
    String display_name,
    List<Double> boundingbox) {}
