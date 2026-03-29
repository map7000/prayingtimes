/*
* Copyright 2025 Mikhail Filatov
* SPDX-License-Identifier: Apache-2.0
*/
package ru.mfilatov.prayingtimes.timecalculator.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpStatusCodeException.class)
  public ResponseEntity<String> handleHttpStatusCodeException(HttpStatusCodeException ex) {
    return ResponseEntity.status(ex.getStatusCode()).body("API error: " + ex.getMessage());
  }

  @ExceptionHandler(ResourceAccessException.class)
  public ResponseEntity<String> handleResourceAccessException(ResourceAccessException ex) {
    return ResponseEntity.status(503).body("Service unavailable: " + ex.getMessage());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
}
