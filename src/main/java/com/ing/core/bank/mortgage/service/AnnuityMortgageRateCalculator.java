package com.ing.core.bank.mortgage.service;

import com.ing.core.bank.mortgage.exception.MortgageRateNotFoundException;
import com.ing.core.bank.mortgage.model.MortgageRate;
import com.ing.core.bank.mortgage.util.DataLoader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnuityMortgageRateCalculator implements MortgageRateCalculator {

  private final DataLoader dataLoader;
  private static final MathContext MATH_CONTEXT = new MathContext(15, RoundingMode.HALF_UP);
  private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
  private static final BigDecimal PAYMENTS_PER_YEAR = BigDecimal.valueOf(12);

  @Override
  public BigDecimal calculate(final Integer maturityPeriod, final BigDecimal loanAmount) throws MortgageRateNotFoundException {
    log.info("Calculating annuity mortgage monthly payment for maturity period={}, loanAmount={}.", maturityPeriod, loanAmount);

    final MortgageRate annualRate = dataLoader.findMortgageRateByMaturityPeriod(maturityPeriod);
    final BigDecimal monthlyRate = annualRate.getInterestRate().divide(HUNDRED, MATH_CONTEXT).divide(PAYMENTS_PER_YEAR, MATH_CONTEXT);
    final BigDecimal monthlyCostsPlusOne = BigDecimal.ONE.add(monthlyRate, MATH_CONTEXT);

    final BigDecimal numerator = calculateNumerator(maturityPeriod, monthlyRate, monthlyCostsPlusOne);
    final BigDecimal denominator = calculateDenominator(maturityPeriod, monthlyCostsPlusOne);
    final BigDecimal monthlyPayment = calculateMonthlyPayment(loanAmount, numerator, denominator);

    log.info("Annuity mortgage monthly payment calculated: {}", monthlyPayment);
    return monthlyPayment;
  }

  private BigDecimal calculateMonthlyPayment(final BigDecimal loanAmount, final BigDecimal numerator, final BigDecimal denominator) {
    return loanAmount.multiply(numerator.divide(denominator, MATH_CONTEXT), MATH_CONTEXT).setScale(2, RoundingMode.HALF_UP);
  }

  private BigDecimal calculateDenominator(final Integer maturityPeriod, final BigDecimal monthlyCostsPlusOne) {
    return monthlyCostsPlusOne.pow(maturityPeriod, MATH_CONTEXT).subtract(BigDecimal.ONE, MATH_CONTEXT);
  }

  private BigDecimal calculateNumerator(final Integer maturityPeriod, final BigDecimal monthlyRate, final BigDecimal monthlyCostsPlusOne) {
    return monthlyRate.multiply(monthlyCostsPlusOne.pow(maturityPeriod, MATH_CONTEXT), MATH_CONTEXT);
  }

}
