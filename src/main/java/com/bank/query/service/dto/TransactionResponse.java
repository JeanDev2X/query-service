package com.bank.query.service.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class TransactionResponse {
//	private String id;
    private String accountNumber;
    private String type;
    private BigDecimal amount;
    private LocalDateTime date;
    
    @JsonCreator
    public TransactionResponse(
            @JsonProperty("accountNumber") String accountNumber,
            @JsonProperty("type") String type,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("date") LocalDateTime date) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }
    
}
