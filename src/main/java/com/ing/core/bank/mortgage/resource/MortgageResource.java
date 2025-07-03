package com.ing.core.bank.mortgage.resource;

import com.ing.core.bank.mortgage.exception.MortgageRateNotFoundException;
import com.ing.core.bank.mortgage.model.MortgageRate;
import com.ing.core.bank.mortgage.resource.request.MortgageCheckRequest;
import com.ing.core.bank.mortgage.resource.response.MortgageCheckDto;
import com.ing.core.bank.mortgage.service.MortgageRateService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class MortgageResource {

  private final MortgageRateService mortgageCheckService;

  @GetMapping("/interest-rates")
  public List<MortgageRate> getMortgageRates() {
    log.info("Get mortgage rates method called.");
    return mortgageCheckService.getMortgageRates();
  }

  @PostMapping("/mortgage-check")
  public ResponseEntity<MortgageCheckDto> mortgageCheck(@RequestBody final MortgageCheckRequest mortgageCheckRequest)
      throws MortgageRateNotFoundException {
    log.info("Mortgage check method called with the following request={}", mortgageCheckRequest);
    return ResponseEntity.ok(mortgageCheckService.mortgageCheck(mortgageCheckRequest));
  }

}
