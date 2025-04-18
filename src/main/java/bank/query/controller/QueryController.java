package bank.query.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import bank.query.dto.AccountDTO;
import bank.query.dto.BalanceSummaryResponse;
import bank.query.dto.CommissionReportResponse;
import bank.query.dto.CustomerSummaryDTO;
import bank.query.dto.GeneralReportRequest;
import bank.query.dto.GeneralReportResponse;
import bank.query.dto.LastMovementsReport;
import bank.query.dto.LastMovementsRequest;
import bank.query.dto.LoanDTO;
import bank.query.dto.TransactionDTO;
import bank.query.service.QueryService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/query")
public class QueryController {
	
	@Autowired
    private QueryService queryService;

	@GetMapping("/balance/account/{accountNumber}")
	public Mono<AccountDTO> getAccountBalance(@PathVariable String accountNumber) {
	    return queryService.getAccountBalance(accountNumber);
	}

    @GetMapping("/balance/credit/{creditNumber}")
    public Mono<LoanDTO> getCreditBalance(@PathVariable String creditNumber) {
        return queryService.getCreditBalance(creditNumber);
    }

    @GetMapping("/movements/account/{accountNumber}")
    public Flux<TransactionDTO> getMovementsByAccount(@PathVariable String accountNumber) {
        return queryService.getMovementsByAccount(accountNumber);
    }

    @GetMapping("/movements/credit/{creditNumber}")
    public Flux<TransactionDTO> getMovementsByCredit(@PathVariable String creditNumber) {
        return queryService.getMovementsByCredit(creditNumber);
    }
    
    // Endpoint para obtener el resumen con el saldo promedio diario
    @GetMapping("/balance/summary/{documentNumber}")
    public Mono<BalanceSummaryResponse> getBalanceSummary(@PathVariable String documentNumber) {
        return queryService.getBalanceSummary(documentNumber);
    }

    @GetMapping("/report/commissions")
    public Flux<CommissionReportResponse> getCommissionReport(
            @RequestParam("from") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate from,
            @RequestParam("to") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate to) {
        return queryService.getCommissionReport(from, to);
    }
    
    @GetMapping("/summary/{documentNumber}")
    public Mono<CustomerSummaryDTO> getCustomerSummary(@PathVariable String documentNumber) {
        return queryService.getCustomerSummary(documentNumber);
    }
    
    @PostMapping("/general-report")
    public Mono<GeneralReportResponse> getGeneralReport(@RequestBody GeneralReportRequest request) {
        return queryService.generateGeneralReport(request);
    }
    
    @PostMapping("/last-card-movements")
    public Mono<LastMovementsReport> getLastCardMovementsByCustomer(@RequestBody LastMovementsRequest request) {
        return queryService.getLastCardMovementsByCustomer(request.getDocumentNumber());
    }
}
