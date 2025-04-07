package com.bank.query.service.dto;


import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccountBalanceResponse {
	private String accountNumber;
    private String documentNumber;
    private BigDecimal balance;
    
    // Asegúrate de que Jackson pueda deserializar la clase correctamente
    @JsonCreator
    public AccountBalanceResponse(
            @JsonProperty("accountNumber") String accountNumber,
            @JsonProperty("documentNumber") String documentNumber,
            @JsonProperty("balance") BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.documentNumber = documentNumber;
        this.balance = balance;
    }
    
}
