package ru.mfilatov.prayingtimes.qiblacalculator.direction;

import ru.mfilatov.prayingtimes.models.GeoLocation;

public interface BearingCalculator {
    double calculateBearing(GeoLocation start, GeoLocation end);
}