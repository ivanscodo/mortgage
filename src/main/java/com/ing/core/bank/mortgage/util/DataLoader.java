package com.ing.core.bank.mortgage.util;

import com.ing.core.bank.mortgage.exception.MortgageRateNotFoundException;
import com.ing.core.bank.mortgage.model.MortgageRate;
import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Setter
@Configuration
@ConfigurationProperties(prefix = "data-loader")
public class DataLoader {

  private List<MortgageRate> mortgageRates;

  public List<MortgageRate> getAllMortgageRates() {
    return mortgageRates;
  }

  public MortgageRate findMortgageRateByMaturityPeriod(final Integer maturityPeriod) throws MortgageRateNotFoundException {
    return mortgageRates.stream()
        .filter(mortgageRate -> mortgageRate.getMaturityPeriod().equals(maturityPeriod))
        .findFirst()
        .orElseThrow(() -> new MortgageRateNotFoundException("Mortgage rate not found for maturity period=" + maturityPeriod));
  }

}
