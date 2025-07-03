package com.ing.core.bank.mortgage.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.ing.core.bank.mortgage.exception.MortgageRateNotFoundException;
import com.ing.core.bank.mortgage.model.MortgageRate;
import com.ing.core.bank.mortgage.resource.request.MortgageCheckRequest;
import com.ing.core.bank.mortgage.resource.response.MortgageCheckDto;
import com.ing.core.bank.mortgage.util.DataLoader;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MortgageRateServiceTest {

  private MortgageRateService mortgageRateService;
  private final BigDecimal loanIncomeTimesLimit = BigDecimal.valueOf(4);

  @Mock
  private AnnuityMortgageRateCalculator annuityMortgageRateCalculator;

  @Mock
  private DataLoader dataLoader;

  @BeforeEach
  void setUp() {
    mortgageRateService = new MortgageRateService(dataLoader, annuityMortgageRateCalculator, loanIncomeTimesLimit);
  }

  @Test
  void should_fail_when_income_validation_fails() throws MortgageRateNotFoundException {
    final BigDecimal loanAmount = BigDecimal.valueOf(100000);
    final int maturityPeriod = 20;
    final MortgageCheckRequest request = MortgageCheckRequest.builder()
        .income(BigDecimal.valueOf(2000))
        .loanValue(loanAmount)
        .maturityPeriod(maturityPeriod)
        .homeValue(BigDecimal.valueOf(150000))
        .build();
    when(annuityMortgageRateCalculator.calculate(maturityPeriod, loanAmount))
        .thenReturn(BigDecimal.valueOf(600));

    final MortgageCheckDto result = mortgageRateService.mortgageCheck(request);

    assertFalse(result.getFeasible());
  }

  @Test
  void should_return_all_mortgage_rates() {
    final List<MortgageRate> mockRates = List.of(
        MortgageRate.builder().build(),
        MortgageRate.builder().build()
    );
    when(dataLoader.getAllMortgageRates()).thenReturn(mockRates);

    final List<MortgageRate> result = mortgageRateService.getMortgageRates();

    assertEquals(mockRates.size(), result.size());
    assertEquals(mockRates, result);
  }

  @Test
  void should_fail_when_loan_limit_validation_fails() throws MortgageRateNotFoundException {
    final BigDecimal loanAmount = BigDecimal.valueOf(300000);
    final int maturityPeriod = 30;
    final MortgageCheckRequest request = MortgageCheckRequest.builder()
        .income(BigDecimal.valueOf(5000))
        .loanValue(loanAmount)
        .maturityPeriod(maturityPeriod)
        .homeValue(BigDecimal.valueOf(200000))
        .build();
    when(annuityMortgageRateCalculator.calculate(maturityPeriod, loanAmount))
        .thenReturn(BigDecimal.valueOf(1000));

    final MortgageCheckDto result = mortgageRateService.mortgageCheck(request);

    assertFalse(result.getFeasible());
    assertEquals(BigDecimal.valueOf(1000), result.getMonthlyCosts());
  }

  @Test
  void should_succeed_when_income_and_loan_limit_validations_pass() throws MortgageRateNotFoundException {
    final int maturityPeriod = 25;
    final BigDecimal loanValue = BigDecimal.valueOf(200000);
    final MortgageCheckRequest request = MortgageCheckRequest.builder()
        .income(BigDecimal.valueOf(10000))
        .loanValue(loanValue)
        .maturityPeriod(maturityPeriod)
        .homeValue(BigDecimal.valueOf(300000))
        .build();
    when(annuityMortgageRateCalculator.calculate(maturityPeriod, loanValue))
        .thenReturn(BigDecimal.valueOf(1500));

    final MortgageCheckDto result = mortgageRateService.mortgageCheck(request);

    assertTrue(result.getFeasible());
    assertEquals(BigDecimal.valueOf(1500), result.getMonthlyCosts());
  }

}
