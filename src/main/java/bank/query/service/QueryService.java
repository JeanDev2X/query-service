package bank.query.service;

import java.time.LocalDate;

import bank.query.dto.AccountDTO;
import bank.query.dto.BalanceSummaryResponse;
import bank.query.dto.CommissionReportResponse;
import bank.query.dto.CustomerSummaryDTO;
import bank.query.dto.GeneralReportRequest;
import bank.query.dto.GeneralReportResponse;
import bank.query.dto.LastMovementsReport;
import bank.query.dto.LoanDTO;
import bank.query.dto.TransactionDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QueryService {
	Mono<AccountDTO> getAccountBalance(String accountNumber);
    Mono<LoanDTO> getCreditBalance(String creditNumber);
    Flux<TransactionDTO> getMovementsByAccount(String accountNumber);
    Flux<TransactionDTO> getMovementsByCredit(String creditNumber);
    Mono<BalanceSummaryResponse> getBalanceSummary(String documentNumber);
    Flux<CommissionReportResponse> getCommissionReport(LocalDate from, LocalDate to);
    Mono<CustomerSummaryDTO> getCustomerSummary(String documentNumber);
    Mono<GeneralReportResponse> generateGeneralReport(GeneralReportRequest request);
    public Mono<LastMovementsReport> getLastCardMovementsByCustomer(String documentNumber);
}
