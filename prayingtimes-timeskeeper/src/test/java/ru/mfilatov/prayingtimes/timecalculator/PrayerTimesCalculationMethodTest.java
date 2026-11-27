/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import ru.mfilatov.prayingtimes.models.PrayerTimesCalculationMethod;

class PrayerTimesCalculationMethodTest {

  @Test
  void mapsEnumNamesToProviderMethodNames() {
    Map<PrayerTimesCalculationMethod, String> expected =
        Map.ofEntries(
            Map.entry(PrayerTimesCalculationMethod.MWL, "mwl"),
            Map.entry(PrayerTimesCalculationMethod.ISNA, "isna"),
            Map.entry(PrayerTimesCalculationMethod.UMM_AL_QURA, "makkah"),
            Map.entry(PrayerTimesCalculationMethod.EGYPTIAN, "egypt"),
            Map.entry(PrayerTimesCalculationMethod.MALAYSIA, "jakim"),
            Map.entry(PrayerTimesCalculationMethod.INDONESIA, "kemenag"),
            Map.entry(PrayerTimesCalculationMethod.LISBON, "portugal"),
            Map.entry(PrayerTimesCalculationMethod.MOONSIGHTING, "moonsighting"),
            Map.entry(PrayerTimesCalculationMethod.TEHRAN, "tehran"),
            Map.entry(PrayerTimesCalculationMethod.JAFARI, "jafari"));

    expected.forEach(
        (method, providerName) -> assertThat(method.providerName()).isEqualTo(providerName));
  }

  @Test
  void everyMethodHasAUniqueProviderName() {
    var providerNames =
        Arrays.stream(PrayerTimesCalculationMethod.values())
            .map(PrayerTimesCalculationMethod::providerName)
            .toList();

    assertThat(providerNames).doesNotContainNull().doesNotHaveDuplicates();
    assertThat(providerNames)
        .allMatch(name -> name.matches("[a-z][a-z0-9_]*"), "lowercase provider identifiers");
    assertThat(providerNames).hasSize(PrayerTimesCalculationMethod.values().length);
  }

  @Test
  void providerNamesAreNotLeakedThroughJackson() throws Exception {
    var mapper = new tools.jackson.databind.ObjectMapper();
    var json = mapper.writeValueAsString(PrayerTimesCalculationMethod.MWL);

    assertThat(json).isEqualTo("\"MWL\"");
    assertThat(json).doesNotContain("providerName");
  }

  @Test
  void lookupHelpersStillWork() {
    assertThat(PrayerTimesCalculationMethod.fromCode(4))
        .isEqualTo(PrayerTimesCalculationMethod.UMM_AL_QURA);
    assertThat(PrayerTimesCalculationMethod.fromName("mwl"))
        .isEqualTo(PrayerTimesCalculationMethod.MWL);
    assertThat(
            Arrays.stream(PrayerTimesCalculationMethod.values())
                .map(PrayerTimesCalculationMethod::getCode)
                .collect(Collectors.toSet()))
        .hasSize(PrayerTimesCalculationMethod.values().length);
  }
}
