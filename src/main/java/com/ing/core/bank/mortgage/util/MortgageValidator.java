package com.ing.core.bank.mortgage.util;

import com.ing.core.bank.mortgage.resource.request.MortgageCheckRequest;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MortgageValidator {

  private MortgageValidator() {
  }

  public static boolean validateIncome(final BigDecimal monthlyCosts, final BigDecimal income, final BigDecimal loanIncomeTimesLimit) {
    return monthlyCosts.multiply(loanIncomeTimesLimit).compareTo(income) <= 0;
  }

  public static boolean validateLoanLimit(final MortgageCheckRequest mortgageCheckRequest) {
    return mortgageCheckRequest.getLoanValue().compareTo(mortgageCheckRequest.getHomeValue()) <= 0;
  }

}
