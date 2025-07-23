/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.apache.commons.math3.util.FastMath;
import ru.mfilatov.prayingtimes.timecalculator.models.CalculationParameters;
import ru.mfilatov.prayingtimes.timecalculator.models.GeoLocation;
import ru.mfilatov.prayingtimes.timecalculator.models.PrayerTimes;
import ru.mfilatov.prayingtimes.timecalculator.utils.MathUtils;

public class MWLCalculationStrategy implements PrayerCalculationStrategy {

  private final CalculationParameters parameters;

  public MWLCalculationStrategy(CalculationParameters parameters) {
    this.parameters = parameters;
  }

  @Override
  public PrayerTimes calculatePrayerTimes(LocalDate date, GeoLocation location) {
    PrayerTimes times = new PrayerTimes();
    ZoneId zoneId = ZoneId.of(location.getTimezone());
    ZonedDateTime zonedDateTime = date.atStartOfDay(zoneId);

    if (parameters.isDaylightSavingEnabled()) {
      zonedDateTime = zonedDateTime.plusMinutes(parameters.getDstOffsetMinutes());
    }

    double julianDate =
        calculateJulianDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    double latitude = location.getLatitude();
    double longitude = location.getLongitude();

    // 1. Calculate sun declination
    double declination = calculateSunDeclination(julianDate);

    // 2. Calculate equation of time
    double eqTime = calculateEquationOfTime(julianDate);

    // 3. Calculate Dhuhr (midday)
    times.setDhuhr(calculateDhuhrTime(zonedDateTime, longitude, eqTime));

    // 4. Calculate other prayer times
    times.setFajr(
        calculateTime(
            zonedDateTime, latitude, declination, parameters.getFajrAngle(), eqTime, false));
    times.setSunrise(calculateTime(zonedDateTime, latitude, declination, 0.833, eqTime, false));
    times.setAsr(
        calculateAsrTime(
            zonedDateTime, latitude, declination, 1, eqTime)); // Shafii method (factor = 1)
    times.setMaghrib(calculateTime(zonedDateTime, latitude, declination, 0.833, eqTime, true));
    times.setIsha(
        calculateTime(
            zonedDateTime, latitude, declination, parameters.getIshaAngle(), eqTime, true));

    // Apply Asr method
    double asrShadowFactor =
        parameters.getAsrMethod() == CalculationParameters.AsrJuristicMethod.HANAFI ? 2 : 1;
    if (parameters.getAsrMethod() == CalculationParameters.AsrJuristicMethod.CUSTOM) {
      asrShadowFactor = parameters.getAsrShadowFactor();
    }
    times.setAsr(
        calculateAsrTime(
            zonedDateTime, location.getLatitude(), declination, asrShadowFactor, eqTime));

    return times;
  }

  private double calculateJulianDate(int year, int month, int day) {
    if (month <= 2) {
      year -= 1;
      month += 12;
    }
    int A = year / 100;
    int B = 2 - A + (A / 4);
    return Math.floor(365.25 * (year + 4716))
        + Math.floor(30.6001 * (month + 1))
        + day
        + B
        - 1524.5;
  }

  private double calculateSunDeclination(double julianDate) {
    double T = (julianDate - 2451545) / 36525;
    double L0 = 280.46645 + 36000.76983 * T;
    double M = 357.52910 + 35999.05030 * T;

    // Normalize to 0-360 range
    L0 = L0 % 360;
    M = M % 360;

    // Convert to radians
    double MRad = Math.toRadians(M);

    // Equation of center
    double C =
        (1.914600 - 0.004817 * T - 0.000014 * T * T) * FastMath.sin(MRad)
            + (0.019993 - 0.000101 * T) * FastMath.sin(2 * MRad)
            + 0.000290 * FastMath.sin(3 * MRad);

    double trueLongitude = L0 + C;
    double trueAnomaly = M + C;

    // Sun declination in radians
    return Math.toRadians(0.0000002675) * trueLongitude;
  }

  private double calculateEquationOfTime(double julianDate) {
    double T = (julianDate - 2451545) / 36525;
    double L0 = 280.46645 + 36000.76983 * T;
    double M = 357.52910 + 35999.05030 * T;
    double e = 0.016708617 - 0.000042037 * T - 0.0000001236 * T * T;

    double y = FastMath.tan(Math.toRadians(23.5) / 2);
    y *= y;

    double eqTime =
        y * FastMath.sin(2 * Math.toRadians(L0))
            - 2 * e * FastMath.sin(Math.toRadians(M))
            + 4 * e * y * FastMath.sin(Math.toRadians(M)) * FastMath.cos(2 * Math.toRadians(L0))
            - 0.5 * y * y * FastMath.sin(4 * Math.toRadians(L0))
            - 1.25 * e * e * FastMath.sin(2 * Math.toRadians(M));

    return Math.toDegrees(eqTime) * 4; // Convert to minutes
  }

  private LocalTime calculateTime(
      ZonedDateTime zonedDateTime,
      double lat,
      double declination,
      double angle,
      double eqTime,
      boolean isSunset) {
    double zenith = angle + 0.8333;
    double hourAngle = calculateHourAngle(lat, declination, zenith, isSunset);
    double delta = isSunset ? hourAngle : -hourAngle;
    return calculateTimeFromAngle(zonedDateTime, delta, eqTime);
  }

  private double calculateHourAngle(
      double lat, double declination, double zenith, boolean isSunset) {
    double latRad = Math.toRadians(lat);
    double decRad = Math.toRadians(declination);
    double zenithRad = Math.toRadians(zenith);

    double cosH =
        (FastMath.cos(zenithRad) - FastMath.sin(latRad) * FastMath.sin(decRad))
            / (FastMath.cos(latRad) * FastMath.cos(decRad));

    if (cosH > 1 || cosH < -1) {
      return Double.NaN; // No sunrise/sunset at this location
    }

    return Math.toDegrees(FastMath.acos(cosH));
  }

  private LocalTime calculateTimeFromAngle(
      ZonedDateTime zonedDateTime, double hourAngle, double eqTime) {
    double timeDiff = 12 * Math.toRadians(hourAngle) / Math.PI;
    double timeUTC = timeDiff + eqTime / 60;

    // Adjust for timezone
    long seconds = (long) (timeUTC * 3600);
    return zonedDateTime.withHour(0).withMinute(0).withSecond(0).plusSeconds(seconds).toLocalTime();
  }

  private LocalTime calculateAsrTime(
      ZonedDateTime zonedDateTime,
      double lat,
      double declination,
      double shadowFactor,
      double eqTime) {
    double angle =
        -MathUtils.acotStable(
            shadowFactor + FastMath.tan(Math.abs(Math.toRadians(lat - declination))));
    return calculateTime(zonedDateTime, lat, declination, Math.toDegrees(angle), eqTime, false);
  }

  private LocalTime calculateDhuhrTime(
      ZonedDateTime zonedDateTime, double longitude, double eqTime) {
    double timeUTC = 12 + eqTime / 60 - (longitude / 15);
    long seconds = (long) (timeUTC * 3600);
    return zonedDateTime.withHour(0).withMinute(0).withSecond(0).plusSeconds(seconds).toLocalTime();
  }
}
