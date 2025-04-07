package com.bank.query.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bank.query.service.dto.AccountBalanceResponse;
import com.bank.query.service.dto.CreditBalanceResponse;
import com.bank.query.service.dto.TransactionResponse;
import com.bank.query.service.service.QueryService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class QueryServiceImpl implements QueryService{
	@Autowired
    private WebClient.Builder webClientBuilder;

    private static final String ACCOUNT_SERVICE_URL = "http://localhost:8021/accounts";
    private static final String CREDIT_SERVICE_URL = "http://localhost:8022/credits";
    private static final String TRANSACTION_SERVICE_URL = "http://localhost:8023/transactions";

    @Override
    public Mono<AccountBalanceResponse> getAccountBalance(String accountNumber) {
    	
        // Obtener el saldo de la cuenta desde el microservicio Account-Service
        return webClientBuilder.build()
        		.get()
                .uri(ACCOUNT_SERVICE_URL + "/by-account-number/{accountNumber}", accountNumber)
                .retrieve()
                .bodyToMono(AccountBalanceResponse.class)  // Asegúrate de que esté devolviendo AccountBalanceResponse
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found")));
    }

    @Override
    public Mono<CreditBalanceResponse> getCreditBalance(String creditNumber) {
        return webClientBuilder.build()
                .get()
                .uri(CREDIT_SERVICE_URL + "/by-credit-number/{creditNumber}", creditNumber)
                .retrieve()
                .bodyToMono(CreditBalanceResponse.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Credit not found")));
    }

    @Override
    public Flux<TransactionResponse> getMovementsByAccount(String accountNumber) {
        return webClientBuilder.build()
                .get()
                .uri(TRANSACTION_SERVICE_URL + "/movements/account/{accountNumber}", accountNumber)
                .retrieve()
                .bodyToFlux(TransactionResponse.class);
    }

    @Override
    public Flux<TransactionResponse> getMovementsByCredit(String creditNumber) {
        return webClientBuilder.build()
                .get()
                .uri(TRANSACTION_SERVICE_URL + "/movements/credit/{creditNumber}", creditNumber)
                .retrieve()
                .bodyToFlux(TransactionResponse.class);
    }
}
