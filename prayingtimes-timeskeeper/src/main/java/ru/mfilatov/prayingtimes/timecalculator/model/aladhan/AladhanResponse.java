/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.model.aladhan;

import java.util.List;

public record AladhanResponse(int code, String status, Data data) {
  public record Data(Timings timings, Date date, Meta meta) {}

  public record Timings(
      String Fajr,
      String Sunrise,
      String Dhuhr,
      String Asr,
      String Sunset,
      String Maghrib,
      String Isha,
      String Imsak,
      String Midnight,
      String Firstthird,
      String Lastthird) {}

  public record Date(String readable, String timestamp, Hijri hijri, Gregorian gregorian) {}

  public record Hijri(
      String date,
      String format,
      String day,
      Weekday weekday,
      Month month,
      String year,
      Designation designation,
      List<String> holidays,
      List<String> adjustedHolidays,
      String method) {}

  public record Gregorian(
      String date,
      String format,
      String day,
      Weekday weekday,
      Month month,
      String year,
      Designation designation,
      boolean lunarSighting) {}

  public record Weekday(String en, String ar) {}

  public record Month(
      int number,
      String en,
      String ar,
      Integer days // Optional since Gregorian month doesn't have days
      ) {}

  public record Designation(String abbreviated, String expanded) {}

  public record Meta(
      double latitude,
      double longitude,
      String timezone,
      Method method,
      String latitudeAdjustmentMethod,
      String midnightMode,
      String school,
      Offset offset) {}

  public record Method(int id, String name, Params params, Location location) {}

  public record Params(int Fajr, int Isha) {}

  public record Location(double latitude, double longitude) {}

  public record Offset(
      int Imsak,
      int Fajr,
      int Sunrise,
      int Dhuhr,
      int Asr,
      int Sunset,
      int Maghrib,
      int Isha,
      int Midnight) {}
}
