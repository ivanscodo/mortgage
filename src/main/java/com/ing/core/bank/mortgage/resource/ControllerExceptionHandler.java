package com.ing.core.bank.mortgage.resource;

import com.ing.core.bank.mortgage.exception.MortgageRateNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

  private static final String ERRORS = "errors";

  @ExceptionHandler(MortgageRateNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handle(final MortgageRateNotFoundException ex) {
    final Map<String, List<String>> result = new HashMap<>();
    result.put(ERRORS, getErrorMessages(ex));
    return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, List<String>>> handle(final HttpMessageNotReadableException ex) {
    final Map<String, List<String>> result = new HashMap<>();
    result.put(ERRORS, getErrorMessages(ex));
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  private List<String> getErrorMessages(final Exception ex) {
    final List<String> errorMessages = List.of(ex.getMessage());
    log.error("An error occurred while processing the request. Error={}", errorMessages);
    return errorMessages;
  }

}
