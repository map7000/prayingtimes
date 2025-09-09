/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.mfilatov.prayingtimes.models.PrayerTimes;
import ru.mfilatov.prayingtimes.models.PrayerTimesCalculationMethod;
import ru.mfilatov.prayingtimes.models.dto.PrayerTimesRequest;
import ru.mfilatov.prayingtimes.timecalculator.model.aladhan.AladhanPrayerTimesRequest;
import ru.mfilatov.prayingtimes.timecalculator.model.aladhan.AladhanResponse;
import ru.mfilatov.prayingtimes.timecalculator.util.PrayerTimeUtils;

@Mapper(componentModel = "spring", imports = PrayerTimeUtils.class)
public interface AladhanPrayerTimesMapper {

  @Mapping(target = "latitude", source = "latitude")
  @Mapping(target = "longitude", source = "longitude")
  @Mapping(target = "method", source = "method", qualifiedByName = "methodToCode")
  @Mapping(target = "timezonestring", source = "timezone")
  @Mapping(target = "shafaq", ignore = true)
  @Mapping(target = "tune", ignore = true)
  @Mapping(target = "school", expression = "java(getDefaultSchool())")
  @Mapping(target = "midnightMode", ignore = true)
  @Mapping(target = "latitudeAdjustmentMethod", ignore = true)
  @Mapping(target = "calendarMethod", ignore = true)
  @Mapping(target = "iso8601", constant = "true")
  AladhanPrayerTimesRequest toPrayerTimesRequest(PrayerTimesRequest simpleRequest);

  @Mapping(
      target = "fajr",
      expression = "java(PrayerTimeUtils.parsePrayerTime(aladhanResponse.data().timings().Fajr()))")
  @Mapping(
      target = "sunrise",
      expression =
          "java(PrayerTimeUtils.parsePrayerTime(aladhanResponse.data().timings().Sunrise()))")
  @Mapping(
      target = "dhuhr",
      expression =
          "java(PrayerTimeUtils.parsePrayerTime(aladhanResponse.data().timings().Dhuhr()))")
  @Mapping(
      target = "asr",
      expression = "java(PrayerTimeUtils.parsePrayerTime(aladhanResponse.data().timings().Asr()))")
  @Mapping(
      target = "sunset",
      expression =
          "java(PrayerTimeUtils.parsePrayerTime(aladhanResponse.data().timings().Sunset()))")
  @Mapping(
      target = "maghrib",
      expression =
          "java(PrayerTimeUtils.parsePrayerTime(aladhanResponse.data().timings().Maghrib()))")
  @Mapping(
      target = "isha",
      expression = "java(PrayerTimeUtils.parsePrayerTime(aladhanResponse.data().timings().Isha()))")
  @Mapping(
      target = "imsak",
      expression =
          "java(PrayerTimeUtils.parsePrayerTime(aladhanResponse.data().timings().Imsak()))")
  @Mapping(
      target = "midnight",
      expression =
          "java(PrayerTimeUtils.parsePrayerTime(aladhanResponse.data().timings().Midnight()))")
  PrayerTimes toPrayerTimes(AladhanResponse aladhanResponse);

  @Named("methodToCode")
  default Integer methodToCode(PrayerTimesCalculationMethod method) {
    return method != null ? method.getCode() : null;
  }

  /** 0 - Shafi 1 - Hanafi */
  default Integer getDefaultSchool() {
    return 1;
  }
}
