/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PrayerTimesCalculationMethod {
  JAFARI(0, "Jafari / Shia Ithna-Ashari"),
  KARACHI(1, "University of Islamic Sciences, Karachi"),
  ISNA(2, "Islamic Society of North America"),
  MWL(3, "Muslim World League"),
  UMM_AL_QURA(4, "Umm Al-Qura University, Makkah"),
  EGYPTIAN(5, "Egyptian General Authority of Survey"),
  TEHRAN(7, "Institute of Geophysics, University of Tehran"),
  GULF(8, "Gulf Region"),
  KUWAIT(9, "Kuwait"),
  QATAR(10, "Qatar"),
  SINGAPORE(11, "Majlis Ugama Islam Singapura, Singapore"),
  FRANCE(12, "Union Organization islamic de France"),
  TURKEY(13, "Diyanet İşleri Başkanlığı, Turkey"),
  RUSSIA(14, "Spiritual Administration of Muslims of Russia"),
  MOONSIGHTING(15, "Moonsighting Committee Worldwide"),
  DUBAI(16, "Dubai (experimental)"),
  MALAYSIA(17, "Jabatan Kemajuan Islam Malaysia (JAKIM)"),
  TUNISIA(18, "Tunisia"),
  ALGERIA(19, "Algeria"),
  INDONESIA(20, "KEMENAG - Kementerian Agama Republik Indonesia"),
  MOROCCO(21, "Morocco"),
  LISBON(22, "Comunidade Islamica de Lisboa"),
  JORDAN(23, "Ministry of Awqaf, Islamic Affairs and Holy Places, Jordan");

  @Getter private final int code;
  @Getter private final String description;

  public static PrayerTimesCalculationMethod fromCode(int code) {
    for (PrayerTimesCalculationMethod method : values()) {
      if (method.code == code) {
        return method;
      }
    }
    throw new IllegalArgumentException("Unknown calculation method code: " + code);
  }

  public static PrayerTimesCalculationMethod fromName(String name) {
    try {
      return PrayerTimesCalculationMethod.valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Unknown calculation method: " + name);
    }
  }
}
