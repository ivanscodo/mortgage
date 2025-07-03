package com.ing.core.bank.mortgage.resource;

import com.ing.core.bank.mortgage.exception.MortgageRateNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

  private static final String ERRORS = "errors";

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, List<String>>> handle(final HttpMessageNotReadableException ex) {
    final Map<String, List<String>> result = new HashMap<>();
    result.put(ERRORS, getErrorMessages(ex));
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, List<String>>> handle(final MethodArgumentNotValidException ex) {
    final Map<String, List<String>> result = new HashMap<>();
    result.put(ERRORS, getErrorMessages(ex));
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MortgageRateNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handle(final MortgageRateNotFoundException ex) {
    final Map<String, List<String>> result = new HashMap<>();
    result.put(ERRORS, getErrorMessages(ex));
    return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
  }

  private List<String> getErrorMessages(final BindException ex) {
    final List<String> errorMessages = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(f -> f.getField() + " - " + f.getDefaultMessage())
        .toList();
    log.error("An error occurred while processing the following request. Error={}", errorMessages);
    return errorMessages;
  }

  private List<String> getErrorMessages(final Exception ex) {
    final List<String> errorMessages = List.of(ex.getMessage());
    log.error("An error occurred while processing the request. Error={}", errorMessages);
    return errorMessages;
  }

}
