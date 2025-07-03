package com.ing.core.bank.mortgage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.ing.core.bank.mortgage.exception.MortgageRateNotFoundException;
import com.ing.core.bank.mortgage.model.MortgageRate;
import com.ing.core.bank.mortgage.util.DataLoader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnnuityMortgageRateCalculatorTest {

  @Mock
  private DataLoader dataLoader;

  @InjectMocks
  private AnnuityMortgageRateCalculator mortgageRateCalculator;

  @InjectMocks
  private LinearMortgageRateCalculator linearMortgageRateCalculator;

  @Test
  void should_calculate_annuity_mortgage_monthly_payment() throws MortgageRateNotFoundException {
    final int maturityPeriod = 60;
    final BigDecimal loanAmount = BigDecimal.valueOf(19000.00);
    when(dataLoader.findMortgageRateByMaturityPeriod(maturityPeriod)).thenReturn(MortgageRate.builder()
        .interestRate(BigDecimal.valueOf(5.0))
        .lastUpdate(LocalDateTime.now())
        .maturityPeriod(maturityPeriod)
        .build());

    final BigDecimal result = mortgageRateCalculator.calculate(maturityPeriod, loanAmount);
    assertThat(result).isEqualTo(BigDecimal.valueOf(358.55));
  }

  @Test
  void should_fail_when_no_mortgage_rate_found_for_maturity_period() throws MortgageRateNotFoundException {
    final int maturityPeriod = 60;
    final BigDecimal loanAmount = BigDecimal.valueOf(19000.00);
    when(dataLoader.findMortgageRateByMaturityPeriod(maturityPeriod)).thenThrow(new MortgageRateNotFoundException("Mortgage rate not found."));
    assertThatThrownBy(() -> mortgageRateCalculator.calculate(maturityPeriod, loanAmount)).isInstanceOf(
        MortgageRateNotFoundException.class);
  }

  @Test
  void should_calculate_annuity_mortgage_monthly_payment_for_long_term_loans() throws MortgageRateNotFoundException {
    final int maturityPeriod = 360;
    final BigDecimal loanAmount = BigDecimal.valueOf(100000.00);
    when(dataLoader.findMortgageRateByMaturityPeriod(maturityPeriod)).thenReturn(MortgageRate.builder()
        .interestRate(BigDecimal.valueOf(12.0))
        .lastUpdate(LocalDateTime.now())
        .maturityPeriod(maturityPeriod)
        .build());
    final BigDecimal result = mortgageRateCalculator.calculate(maturityPeriod, loanAmount);
    assertThat(BigDecimal.valueOf(1028.61)).isEqualTo(result);
  }

  @Test
  void should_calculate_annuity_mortgage_monthly_payment_for_short_term_loans() throws MortgageRateNotFoundException {
    final int maturityPeriod = 12;
    final BigDecimal loanAmount = BigDecimal.valueOf(5000.00);
    when(dataLoader.findMortgageRateByMaturityPeriod(maturityPeriod)).thenReturn(MortgageRate.builder()
        .interestRate(BigDecimal.valueOf(3.5))
        .lastUpdate(LocalDateTime.now())
        .maturityPeriod(maturityPeriod)
        .build());
    final BigDecimal result = mortgageRateCalculator.calculate(maturityPeriod, loanAmount);
    assertThat(BigDecimal.valueOf(424.61)).isEqualTo(result);
  }

  @Test
  void should_calculate_annuity_mortgage_monthly_payment_for_very_long_term_loans() throws MortgageRateNotFoundException {
    final int maturityPeriod = 300;
    final BigDecimal loanAmount = BigDecimal.valueOf(350000.00);
    when(dataLoader.findMortgageRateByMaturityPeriod(maturityPeriod)).thenReturn(MortgageRate.builder()
        .interestRate(BigDecimal.valueOf(4.2))
        .lastUpdate(LocalDateTime.now())
        .maturityPeriod(maturityPeriod)
        .build());
    final BigDecimal result = mortgageRateCalculator.calculate(maturityPeriod, loanAmount);

    //asserting both results are equal - compare needs to be used instead of assertEquals
    assertThat(BigDecimal.valueOf(1886.30).compareTo(result)).isEqualTo(0);
  }

  @Test
  void should_fail_when_no_mortgage_rate_found_for_linear_payment() throws MortgageRateNotFoundException {
    final int maturityPeriod = 36;
    final BigDecimal loanAmount = BigDecimal.valueOf(15000.00);
    when(dataLoader.findMortgageRateByMaturityPeriod(maturityPeriod)).thenThrow(new MortgageRateNotFoundException("Mortgage rate not found."));

    assertThatThrownBy(() -> mortgageRateCalculator.calculate(maturityPeriod, loanAmount)).isInstanceOf(
        MortgageRateNotFoundException.class);
  }

  @Test
  void should_fail_when_no_implementation_exists_of_a_given_method_to_calculate_payments() {
    final int maturityPeriod = 36;
    final BigDecimal loanAmount = BigDecimal.valueOf(15000.00);

    assertThatThrownBy(() -> linearMortgageRateCalculator.calculate(maturityPeriod, loanAmount)).isInstanceOf(
        UnsupportedOperationException.class);
  }

}
