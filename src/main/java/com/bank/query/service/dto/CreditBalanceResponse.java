package com.bank.query.service.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditBalanceResponse {
	private String creditNumber;
    private String documentNumber;
    private String type;
    private BigDecimal balance;
    
    @JsonCreator
    public CreditBalanceResponse(
            @JsonProperty("creditNumber") String creditNumber,
            @JsonProperty("documentNumber") String documentNumber,
            @JsonProperty("type") String type,
            @JsonProperty("balance") BigDecimal balance) {
        this.creditNumber = creditNumber;
        this.documentNumber = documentNumber;
        this.type = type;
        this.balance = balance;
    }
    
}
