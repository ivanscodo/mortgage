package com.ing.core.bank.mortgage.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ing.core.bank.mortgage.exception.MortgageRateNotFoundException;
import com.ing.core.bank.mortgage.model.MortgageRate;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class DataLoaderTest {

  @Test
  void should_return_correct_mortgageRate_when_found() throws MortgageRateNotFoundException {
    final DataLoader dataLoader = new DataLoader();
    final MortgageRate rate1 = MortgageRate.builder().maturityPeriod(5).interestRate(BigDecimal.valueOf(3.5)).build();
    final MortgageRate rate2 = MortgageRate.builder().maturityPeriod(10).interestRate(BigDecimal.valueOf(4.0)).build();
    dataLoader.setMortgageRates(List.of(rate1, rate2));

    final MortgageRate result = dataLoader.findMortgageRateByMaturityPeriod(5);

    assertNotNull(result);
    assertEquals(5, result.getMaturityPeriod());
    assertEquals(BigDecimal.valueOf(3.5), result.getInterestRate());
  }

  @Test
  void should_throw_exception_when_not_found() {
    final DataLoader dataLoader = new DataLoader();
    final MortgageRate rate1 = MortgageRate.builder().maturityPeriod(5).interestRate(BigDecimal.valueOf(3.5)).build();
    final MortgageRate rate2 = MortgageRate.builder().maturityPeriod(10).interestRate(BigDecimal.valueOf(4.0)).build();
    dataLoader.setMortgageRates(List.of(rate1, rate2));

    final MortgageRateNotFoundException exception = assertThrows(
        MortgageRateNotFoundException.class,
        () -> dataLoader.findMortgageRateByMaturityPeriod(15)
    );
    assertEquals("Mortgage rate not found for maturity period=15", exception.getMessage());
  }

  @Test
  void should_throw_exception_when_an_empty_list() {
    final DataLoader dataLoader = new DataLoader();
    dataLoader.setMortgageRates(List.of());

    final MortgageRateNotFoundException exception = assertThrows(
        MortgageRateNotFoundException.class,
        () -> dataLoader.findMortgageRateByMaturityPeriod(5)
    );
    assertEquals("Mortgage rate not found for maturity period=5", exception.getMessage());
  }
}
