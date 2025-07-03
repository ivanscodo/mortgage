package com.ing.core.bank.mortgage.service;

import com.ing.core.bank.mortgage.exception.MortgageRateNotFoundException;
import java.math.BigDecimal;

public class LinearMortgageRateCalculator implements MortgageRateCalculator {

  @Override
  public BigDecimal calculate(Integer maturityPeriod, BigDecimal loanAmount) throws MortgageRateNotFoundException {
    throw new UnsupportedOperationException("Method not defined.");
  }

}
