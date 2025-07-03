package com.ing.core.bank.mortgage.service;

import com.ing.core.bank.mortgage.exception.MortgageRateNotFoundException;
import com.ing.core.bank.mortgage.model.MortgageRate;
import com.ing.core.bank.mortgage.resource.request.MortgageCheckRequest;
import com.ing.core.bank.mortgage.resource.response.MortgageCheckDto;
import com.ing.core.bank.mortgage.util.DataLoader;
import com.ing.core.bank.mortgage.util.MortgageValidator;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MortgageRateService {

  private final DataLoader dataLoader;
  private final MortgageRateCalculator annuityMortgageRateCalculator;
  private final BigDecimal loanIncomeTimesLimit;

  public MortgageRateService(
      final DataLoader dataLoader,
      final MortgageRateCalculator annuityMortgageRateCalculator,
      @Value("${loan.income-times-limit}") final BigDecimal loanIncomeTimesLimit
  ) {
    this.dataLoader = dataLoader;
    this.annuityMortgageRateCalculator = annuityMortgageRateCalculator;
    this.loanIncomeTimesLimit = loanIncomeTimesLimit;
  }

  public MortgageCheckDto mortgageCheck(final MortgageCheckRequest mortgageCheckRequest) throws MortgageRateNotFoundException {
    final BigDecimal monthlyCosts = calculateMortgage(mortgageCheckRequest);
    final boolean feasible =
        MortgageValidator.validateIncome(monthlyCosts, mortgageCheckRequest.getIncome(), loanIncomeTimesLimit)
            && MortgageValidator.validateLoanLimit(
            mortgageCheckRequest);
    log.info("Mortgage check returning the following results. Feasible={}, monthlyCosts={}.", feasible, monthlyCosts);
    return MortgageCheckDto.builder()
        .monthlyCosts(monthlyCosts)
        .feasible(feasible)
        .build();
  }

  public List<MortgageRate> getMortgageRates() {
    log.info("Returning all mortgage rates stored in memory by the dataLoader.");
    return dataLoader.getAllMortgageRates();
  }

  private BigDecimal calculateMortgage(final MortgageCheckRequest mortgageCheckRequest) throws MortgageRateNotFoundException {
    return annuityMortgageRateCalculator.calculate(mortgageCheckRequest.getMaturityPeriod(),
        mortgageCheckRequest.getLoanValue());
  }

}
