package com.ing.core.bank.mortgage.resource.request;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class MortgageCheckRequest {

  @NonNull
  private final BigDecimal income;

  @NonNull
  private final Integer maturityPeriod;

  @NonNull
  private final BigDecimal loanValue;

  @NonNull
  private final BigDecimal homeValue;

}
