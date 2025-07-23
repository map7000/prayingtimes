/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.qiblacalculator.geomagnetic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WMCCoefficientLoader {
  /**
   * Loads WMM coefficients from a file
   *
   * @param filePath Path to coefficient file
   * @return List of coefficient entries
   */
  public static List<double[]> loadFromFile(Path filePath) throws IOException {
    List<double[]> coefficients = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(filePath)) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.trim().isEmpty() || line.startsWith("9999999")) {
          continue;
        }

        // Parse coefficient line
        double[] coef = parseCoefficientLine(line);
        if (coef != null) {
          coefficients.add(coef);
        }
      }
    }

    return coefficients;
  }

  /** Loads WMM coefficients from classpath resource */
  public static List<double[]> loadFromResource(String resourcePath) throws IOException {
    List<double[]> coefficients = new ArrayList<>();

    try (InputStream is = WMCCoefficientLoader.class.getResourceAsStream(resourcePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.trim().isEmpty() || line.startsWith("9999999")) {
          continue;
        }

        double[] coef = parseCoefficientLine(line);
        if (coef != null) {
          coefficients.add(coef);
        }
      }
    }

    return coefficients;
  }

  private static double[] parseCoefficientLine(String line) {
    // Example line: "  1  0  -29404.5       0.0       6.7        0.0"
    String[] parts = line.trim().split("\\s+");
    if (parts.length < 6) return null;

    try {
      return new double[] {
        Double.parseDouble(parts[0]),
        Double.parseDouble(parts[1]),
        Double.parseDouble(parts[2]),
        Double.parseDouble(parts[3]),
        Double.parseDouble(parts[4]),
        Double.parseDouble(parts[5])
      };
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
