package bank.query.service;

import java.time.LocalDate;

import bank.query.dto.AccountBalanceResponse;
import bank.query.dto.BalanceSummaryResponse;
import bank.query.dto.CommissionReportResponse;
import bank.query.dto.CreditBalanceResponse;
import bank.query.dto.TransactionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QueryService {
	Mono<AccountBalanceResponse> getAccountBalance(String accountNumber);
    Mono<CreditBalanceResponse> getCreditBalance(String creditNumber);
    Flux<TransactionResponse> getMovementsByAccount(String accountNumber);
    Flux<TransactionResponse> getMovementsByCredit(String creditNumber);
    Mono<BalanceSummaryResponse> getBalanceSummary(String documentNumber);
    Flux<CommissionReportResponse> getCommissionReport(LocalDate from, LocalDate to);
}
