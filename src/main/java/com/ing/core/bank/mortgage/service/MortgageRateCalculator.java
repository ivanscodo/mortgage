package com.ing.core.bank.mortgage.service;

import com.ing.core.bank.mortgage.exception.MortgageRateNotFoundException;
import java.math.BigDecimal;

public interface MortgageRateCalculator {

  BigDecimal calculate(Integer maturityPeriod, BigDecimal loanAmount) throws MortgageRateNotFoundException;

}
