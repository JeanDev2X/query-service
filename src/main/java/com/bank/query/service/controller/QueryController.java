package com.bank.query.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bank.query.service.dto.AccountBalanceResponse;
import com.bank.query.service.dto.CreditBalanceResponse;
import com.bank.query.service.dto.TransactionResponse;
import com.bank.query.service.service.QueryService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/query")
public class QueryController {
	
	@Autowired
    private QueryService queryService;

	@GetMapping("/balance/account/{accountNumber}")
	public Mono<AccountBalanceResponse> getAccountBalance(@PathVariable String accountNumber) {
	    return queryService.getAccountBalance(accountNumber);
	}

    @GetMapping("/balance/credit/{creditNumber}")
    public Mono<CreditBalanceResponse> getCreditBalance(@PathVariable String creditNumber) {
        return queryService.getCreditBalance(creditNumber);
    }

    @GetMapping("/movements/account/{accountNumber}")
    public Flux<TransactionResponse> getMovementsByAccount(@PathVariable String accountNumber) {
        return queryService.getMovementsByAccount(accountNumber);
    }

    @GetMapping("/movements/credit/{creditNumber}")
    public Flux<TransactionResponse> getMovementsByCredit(@PathVariable String creditNumber) {
        return queryService.getMovementsByCredit(creditNumber);
    }

}
