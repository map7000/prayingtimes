/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculatoor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.mfilatov.prayingtimes.timecalculator.clients.OpenStreetMapSearchClient;

@SpringBootTest
public class OpenStreetMapClientTestTest {
  @Autowired OpenStreetMapSearchClient openStreetMapSearchClient;

  @Test
  void getCityLocationJsonV2Test() {
    var response =
        openStreetMapSearchClient.getCityLocation("moscow", "Russia", "jsonv2", "1").getFirst();

    assertThat(response.lat()).isEqualTo(55.7505412);
    assertThat(response.lon()).isEqualTo(37.6174782);
    assertThat(response.name()).isEqualTo("Москва");
  }
}
