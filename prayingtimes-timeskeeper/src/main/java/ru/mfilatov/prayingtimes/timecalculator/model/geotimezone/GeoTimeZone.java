/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.model.geotimezone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.OffsetTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.key.ZonedDateTimeKeyDeserializer;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public record GeoTimeZone(
    Double latitude,
    Double longitude,
    String location,
    @JsonProperty("country_iso") String countryIso,
    @JsonProperty("iana_timezone") String ianaTimezone,
    @JsonProperty("timezone_abbreviation") String timezoneAbbreviation,
    @JsonProperty("dst_abbreviation") String dstAbbreviation,
    String offset,
    @JsonProperty("dst_offset") String dstOffset,
    @JsonProperty("current_local_datetime")
        LocalDateTime currentLocalDatetime,
    @JsonProperty("current_utc_datetime")
        ZonedDateTime currentUtcDatetime) {}
