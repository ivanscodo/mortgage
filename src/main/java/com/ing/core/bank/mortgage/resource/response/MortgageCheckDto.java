package com.ing.core.bank.mortgage.resource.response;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MortgageCheckDto {

  private final Boolean feasible;
  private final BigDecimal monthlyCosts;

}
