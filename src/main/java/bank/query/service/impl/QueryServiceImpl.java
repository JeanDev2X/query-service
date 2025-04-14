package bank.query.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import bank.query.dto.AccountBalanceResponse;
import bank.query.dto.BalanceSummaryResponse;
import bank.query.dto.CommissionReportResponse;
import bank.query.dto.CreditBalanceResponse;
import bank.query.dto.TransactionResponse;
import bank.query.service.QueryService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class QueryServiceImpl implements QueryService{
	@Autowired
    private WebClient.Builder webClientBuilder;

	private static final String CUSTOMER_SERVICE_URL = "http://localhost:8020/customers";
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
    
    @Override
    public Mono<BalanceSummaryResponse> getBalanceSummary(String documentNumber) {
        return getAccountsByCustomer(documentNumber)
                .flatMap(accounts -> {
                    // Obtiene el balance de cada cuenta
                    List<Mono<AccountBalanceResponse>> accountBalances = accounts.stream()
                            .map(account -> getAccountBalance(account.getAccountNumber())) // Llamada al balance de cuenta
                            .collect(Collectors.toList());

                    // Espera por todas las respuestas y calcula el total del balance
                    return Mono.zip(accountBalances, (Object[] responses) -> {
                        // Convierte las respuestas a List<AccountBalanceResponse>
                        List<AccountBalanceResponse> balanceResponses = Arrays.stream(responses)
                                .map(response -> (AccountBalanceResponse) response)
                                .collect(Collectors.toList());

                        // Calcula el total del balance
                        BigDecimal totalBalance = balanceResponses.stream()
                                .map(AccountBalanceResponse::getBalance)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        return new BalanceSummaryResponse(totalBalance);
                    });
                });
    }
    
    // Método para obtener cuentas del cliente por su documento
    private Mono<List<AccountBalanceResponse>> getAccountsByCustomer(String documentNumber) {
        return webClientBuilder.baseUrl(ACCOUNT_SERVICE_URL)
                .build()
                .get()
                .uri("/document/{documentNumber}", documentNumber)
                .retrieve()
                .bodyToFlux(AccountBalanceResponse.class)  // Cambiado a AccountBalanceResponse
                .collectList();
    }
    
    @Override
    public Flux<CommissionReportResponse> getCommissionReport(LocalDate from, LocalDate to) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String formattedStart = from.format(formatter);
        String formattedEnd = to.format(formatter);

        return webClientBuilder.build()
                .get()
                .uri(TRANSACTION_SERVICE_URL + "/commission-report?start={start}&end={end}", formattedStart, formattedEnd)
                .retrieve()
                .bodyToFlux(CommissionReportResponse.class); // <-- Aquí la corrección clave
    }
    
}
