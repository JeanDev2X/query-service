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

import bank.query.dto.AccountDTO;
import bank.query.dto.BalanceSummaryResponse;
import bank.query.dto.CommissionReportResponse;
import bank.query.dto.CreditCardDTO;
import bank.query.dto.CustomerSummaryDTO;
import bank.query.dto.DebitCardDTO;
import bank.query.dto.GeneralReportRequest;
import bank.query.dto.GeneralReportResponse;
import bank.query.dto.LastMovementsReport;
import bank.query.dto.CustomerDTO;
import bank.query.dto.LoanDTO;
import bank.query.dto.TransactionDTO;
import bank.query.service.QueryService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class QueryServiceImpl implements QueryService{
	@Autowired
    private WebClient.Builder webClientBuilder;

	private static final String CUSTOMER_SERVICE_URL = "http://localhost:8020/customers";
    private static final String ACCOUNT_SERVICE_URL = "http://localhost:8021/accounts";
    private static final String DEBIT_SERVICE_URL = "http://localhost:8021/debit-cards";
    private static final String CREDIT_SERVICE_URL = "http://localhost:8022/loans";
    private static final String TRANSACTION_SERVICE_URL = "http://localhost:8023/transactions";

    @Override
    public Mono<AccountDTO> getAccountBalance(String accountNumber) {
    	
        // Obtener el saldo de la cuenta desde el microservicio Account-Service
        return webClientBuilder.build()
        		.get()
                .uri(ACCOUNT_SERVICE_URL + "/by-account-number/{accountNumber}", accountNumber)
                .retrieve()
                .bodyToMono(AccountDTO.class)  // Asegúrate de que esté devolviendo AccountBalanceResponse
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found")));
    }

    @Override
    public Mono<LoanDTO> getCreditBalance(String creditNumber) {
        return webClientBuilder.build()
                .get()
                .uri(CREDIT_SERVICE_URL + "/by-credit-number/{creditNumber}", creditNumber)
                .retrieve()
                .bodyToMono(LoanDTO.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Credit not found")));
    }

    @Override
    public Flux<TransactionDTO> getMovementsByAccount(String accountNumber) {
        return webClientBuilder.build()
                .get()
                .uri(TRANSACTION_SERVICE_URL + "/movements/account/{accountNumber}", accountNumber)
                .retrieve()
                .bodyToFlux(TransactionDTO.class);
    }

    @Override
    public Flux<TransactionDTO> getMovementsByCredit(String creditNumber) {
        return webClientBuilder.build()
                .get()
                .uri(TRANSACTION_SERVICE_URL + "/movements/credit/{creditNumber}", creditNumber)
                .retrieve()
                .bodyToFlux(TransactionDTO.class);
    }
    
    @Override
    public Mono<BalanceSummaryResponse> getBalanceSummary(String documentNumber) {
        return getAccountsByCustomer(documentNumber)
                .flatMap(accounts -> {
                    // Obtiene el balance de cada cuenta
                    List<Mono<AccountDTO>> accountBalances = accounts.stream()
                            .map(account -> getAccountBalance(account.getAccountNumber())) // Llamada al balance de cuenta
                            .collect(Collectors.toList());

                    // Espera por todas las respuestas y calcula el total del balance
                    return Mono.zip(accountBalances, (Object[] responses) -> {
                        // Convierte las respuestas a List<AccountBalanceResponse>
                        List<AccountDTO> balanceResponses = Arrays.stream(responses)
                                .map(response -> (AccountDTO) response)
                                .collect(Collectors.toList());

                        // Calcula el total del balance
                        BigDecimal totalBalance = balanceResponses.stream()
                                .map(AccountDTO::getBalance)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        return new BalanceSummaryResponse(totalBalance);
                    });
                });
    }
    
    // Método para obtener cuentas del cliente por su documento
    private Mono<List<AccountDTO>> getAccountsByCustomer(String documentNumber) {
        return webClientBuilder.baseUrl(ACCOUNT_SERVICE_URL)
                .build()
                .get()
                .uri("/document/{documentNumber}", documentNumber)
                .retrieve()
                .bodyToFlux(AccountDTO.class)  // Cambiado a AccountBalanceResponse
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

    //Reportes partes 2
    
	@Override
	public Mono<CustomerSummaryDTO> getCustomerSummary(String documentNumber) {
		WebClient webClient = webClientBuilder.build();

    return webClient.get()
            .uri(CUSTOMER_SERVICE_URL + "/document/{doc}", documentNumber)
            .retrieve()
            .bodyToMono(CustomerDTO.class)
            .flatMap(customer -> {
                Mono<List<AccountDTO>> accounts = webClient.get()
                        .uri(ACCOUNT_SERVICE_URL + "/document/{doc}", documentNumber)
                        .retrieve()
                        .bodyToFlux(AccountDTO.class)
                        .collectList();

                Mono<List<CreditCardDTO>> cards = webClient.get()
                        .uri(CREDIT_SERVICE_URL + "/credit-cards/by-document/{doc}", documentNumber)
                        .retrieve()
                        .bodyToFlux(CreditCardDTO.class)
                        .collectList();

                Mono<List<LoanDTO>> loans = webClient.get()
                        .uri(CREDIT_SERVICE_URL + "/loans/by-document/{doc}", documentNumber)
                        .retrieve()
                        .bodyToFlux(LoanDTO.class)
                        .collectList();

                return Mono.zip(accounts, cards, loans)
                        .map(tuple -> {
                            CustomerSummaryDTO summary = new CustomerSummaryDTO();
                            summary.setDocumentNumber(documentNumber);
                            summary.setFullName(customer.getFirstName() + " " + customer.getLastName());
                            summary.setCustomerType(customer.getCustomerType());
                            summary.setAccounts(tuple.getT1());
                            summary.setCreditCards(tuple.getT2());
                            summary.setLoans(tuple.getT3());
                            return summary;
                        });
            });
	}

	@Override
	public Mono<GeneralReportResponse> generateGeneralReport(GeneralReportRequest request) {
		WebClient webClient = webClientBuilder.build();

	    return webClient.get()
	        .uri(TRANSACTION_SERVICE_URL + "/by-product-and-date", uriBuilder ->
	            uriBuilder
	                .queryParam("productType", request.getProductType())
	                .queryParam("startDate", request.getStartDate())
	                .queryParam("endDate", request.getEndDate())
	                .build())
	        .retrieve()
	        .bodyToFlux(TransactionDTO.class)
	        .collectList()
	        .map(transactions -> new GeneralReportResponse(request.getProductType(), transactions));
	}

	@Override
	public Mono<LastMovementsReport> getLastCardMovementsByCustomer(String documentNumber) {
		WebClient webClient = webClientBuilder.build();

	    Mono<List<String>> debitCardNumbers = webClient.get()
	        .uri(DEBIT_SERVICE_URL + "/by-document/{doc}", documentNumber)
	        .retrieve()
	        .bodyToFlux(DebitCardDTO.class)
	        .map(DebitCardDTO::getCardNumber)
	        .collectList();

	    Mono<List<String>> creditCardNumbers = webClient.get()
	        .uri(CREDIT_SERVICE_URL + "/credit-cards/by-document/{doc}", documentNumber)
	        .retrieve()
	        .bodyToFlux(CreditCardDTO.class)
	        .map(CreditCardDTO::getCardsNumber)
	        .collectList();

	    return Mono.zip(debitCardNumbers, creditCardNumbers)
	        .flatMap(tuple -> {
	            List<String> debitCards = tuple.getT1();
	            List<String> creditCards = tuple.getT2();

	            Mono<List<TransactionDTO>> debitMovements = webClient.post()
	            	    .uri(TRANSACTION_SERVICE_URL + "/last-movements/by-cards")
	            	    .bodyValue(debitCards)
	            	    .retrieve()
	            	    .bodyToFlux(TransactionDTO.class)
	            	    .doOnNext(tx -> System.out.println("Debit TX: " + tx)) // <--- Añade esto
	            	    .collectList();

	            Mono<List<TransactionDTO>> creditMovements = webClient.post()
	                .uri(TRANSACTION_SERVICE_URL + "/last-movements/by-cards")
	                .bodyValue(creditCards)
	                .retrieve()
	                .bodyToFlux(TransactionDTO.class)
	                .doOnNext(tx -> System.out.println("Debit TX: " + tx)) // <--- Añade esto
	                .collectList();

	            return Mono.zip(debitMovements, creditMovements)
	                .map(txTuple -> new LastMovementsReport(txTuple.getT1(), txTuple.getT2()));
	        });
	}
    
}
