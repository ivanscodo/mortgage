package com.ing.core.bank.mortgage.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MortgageRate {

    private final Integer maturityPeriod;
    private final BigDecimal interestRate;
    private final LocalDateTime lastUpdate;

}
