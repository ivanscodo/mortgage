package com.ing.core.bank.mortgage.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ing.core.bank.mortgage.model.MortgageRate;
import com.ing.core.bank.mortgage.resource.request.MortgageCheckRequest;
import com.ing.core.bank.mortgage.resource.response.MortgageCheckDto;
import java.math.BigDecimal;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MortgageResourceIT {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void should_return_all_mortgage_rates_when_called_with_GET_method() {
    final ResponseEntity<List<MortgageRate>> response = restTemplate.exchange(
        "/api/interest-rates",
        HttpMethod.GET,
        null,
        new ParameterizedTypeReference<>() {
        }
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  void should_return_feasible_when_called_with_valid_income_and_loan() {
    final MortgageCheckRequest request = MortgageCheckRequest.builder()
        .income(BigDecimal.valueOf(10000))
        .loanValue(BigDecimal.valueOf(200000))
        .maturityPeriod(300)
        .homeValue(BigDecimal.valueOf(300000))
        .build();

    final ResponseEntity<MortgageCheckDto> response = restTemplate.postForEntity(
        "/api/mortgage-check",
        request,
        MortgageCheckDto.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertNotNull(response.getBody().getMonthlyCosts());
    assertTrue(response.getBody().getFeasible());
  }

  @Test
  void should_return_not_feasible_when_called_with_invalid_income_and_loan() {
    final MortgageCheckRequest request = MortgageCheckRequest.builder()
        .income(BigDecimal.valueOf(5000))
        .loanValue(BigDecimal.valueOf(300000))
        .maturityPeriod(360)
        .homeValue(BigDecimal.valueOf(200000))
        .build();

    final ResponseEntity<MortgageCheckDto> response = restTemplate.postForEntity(
        "/api/mortgage-check",
        request,
        MortgageCheckDto.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertFalse(response.getBody().getFeasible());
  }

  @Test
  void should_return_bad_request_when_called_with_null_income() throws JSONException {
    final JSONObject checkRequest = new JSONObject();
    checkRequest.put("income", null);
    checkRequest.put("loanValue", BigDecimal.valueOf(200000));
    checkRequest.put("maturityPeriod", 300);
    checkRequest.put("homeValue", BigDecimal.valueOf(300000));

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    final HttpEntity<String> httpRequest =
        new HttpEntity<>(checkRequest.toString(), headers);

    final ResponseEntity<MortgageCheckDto> response = restTemplate.postForEntity(
        "/api/mortgage-check",
        httpRequest,
        MortgageCheckDto.class
    );

    assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());
  }

  @Test
  void should_return_not_feasible_when_called_with_a_low_income() {
    final MortgageCheckRequest request = MortgageCheckRequest.builder()
        .income(BigDecimal.valueOf(2000))
        .loanValue(BigDecimal.valueOf(100000))
        .maturityPeriod(240)
        .homeValue(BigDecimal.valueOf(150000))
        .build();

    final ResponseEntity<MortgageCheckDto> response = restTemplate.postForEntity(
        "/api/mortgage-check",
        request,
        MortgageCheckDto.class
    );

    assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getFeasible()).isFalse();
  }
}
