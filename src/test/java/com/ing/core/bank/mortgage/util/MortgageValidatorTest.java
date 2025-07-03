package com.ing.core.bank.mortgage.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ing.core.bank.mortgage.resource.request.MortgageCheckRequest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MortgageValidatorTest {

  private final BigDecimal loanIncomeTimesLimit = BigDecimal.valueOf(4);

  @Test
  void should_return_true_when_income_is_sufficient() {
    final BigDecimal monthlyCosts = BigDecimal.valueOf(1000);
    final BigDecimal income = BigDecimal.valueOf(7000);

    final boolean result = MortgageValidator.validateIncome(monthlyCosts, income, loanIncomeTimesLimit);

    assertTrue(result, "Expected validateIncome() to return true when income is sufficient.");
  }

  @Test
  void should_return_false_when_income_is_insufficient() {
    final BigDecimal monthlyCosts = BigDecimal.valueOf(2000);
    final BigDecimal income = BigDecimal.valueOf(5000);

    final boolean result = MortgageValidator.validateIncome(monthlyCosts, income, loanIncomeTimesLimit);

    assertFalse(result, "Expected validateIncome() to return false when income is insufficient.");
  }

  @Test
  void should_return_true_when_income_equals_limit() {
    final BigDecimal monthlyCosts = BigDecimal.valueOf(2000);
    final BigDecimal income = loanIncomeTimesLimit.multiply(monthlyCosts);

    final boolean result = MortgageValidator.validateIncome(monthlyCosts, income, loanIncomeTimesLimit);

    assertTrue(result, "Expected validateIncome to return true when income equals the limit.");
  }

  @Test
  void should_return_true_when_income_is_less_than_limit() {
    final MortgageCheckRequest mortgageCheckRequest = MortgageCheckRequest.builder()
        .loanValue(BigDecimal.valueOf(250000))
        .homeValue(BigDecimal.valueOf(250000))
        .income(BigDecimal.valueOf(7000))
        .maturityPeriod(30)
        .build();

    final boolean result = MortgageValidator.validateLoanLimit(mortgageCheckRequest);

    assertTrue(result, "Expected validateLoanLimit() to return true when loan value equals home value.");
  }

  @Test
  void should_return_true_when_income_is_greater_than_limit() {
    final MortgageCheckRequest mortgageCheckRequest = MortgageCheckRequest.builder()
        .loanValue(BigDecimal.valueOf(200000))
        .homeValue(BigDecimal.valueOf(250000))
        .income(BigDecimal.valueOf(7000))
        .maturityPeriod(30)
        .build();

    final boolean result = MortgageValidator.validateLoanLimit(mortgageCheckRequest);

    assertTrue(result, "Expected validateLoanLimit() to return true when loan value is less than home value.");
  }

  @Test
  void should_return_false_when_income_is_greater_than_limit_and_loan_value_is_greater_than_home_value() {
    final MortgageCheckRequest mortgageCheckRequest = MortgageCheckRequest.builder()
        .loanValue(BigDecimal.valueOf(300000))
        .homeValue(BigDecimal.valueOf(250000))
        .income(BigDecimal.valueOf(7000))
        .maturityPeriod(30)
        .build();

    final boolean result = MortgageValidator.validateLoanLimit(mortgageCheckRequest);

    assertFalse(result, "Expected validateLoanLimit() to return false when loan value is greater than home value.");
  }
}
