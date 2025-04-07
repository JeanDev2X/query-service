package com.bank.query.service.service;

import com.bank.query.service.dto.AccountBalanceResponse;
import com.bank.query.service.dto.CreditBalanceResponse;
import com.bank.query.service.dto.TransactionResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QueryService {
	Mono<AccountBalanceResponse> getAccountBalance(String accountNumber);
    Mono<CreditBalanceResponse> getCreditBalance(String creditNumber);
    Flux<TransactionResponse> getMovementsByAccount(String accountNumber);
    Flux<TransactionResponse> getMovementsByCredit(String creditNumber);
}
