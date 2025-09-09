/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.direction;

import ru.mfilatov.prayingtimes.models.GeoLocation;

public interface BearingCalculator {
  double calculateBearing(GeoLocation start, GeoLocation end);
}
