package bank.query.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditCardDTO {
	private String cardsNumber;
    private BigDecimal creditLimit;
    private BigDecimal balance;
    
    @JsonCreator
    public CreditCardDTO(
            @JsonProperty("cardsNumber") String cardsNumber,
            @JsonProperty("creditLimit") BigDecimal creditLimit,
            @JsonProperty("balance") BigDecimal balance) {
        this.cardsNumber = cardsNumber;
        this.creditLimit = creditLimit;
        this.balance = balance;
    }
    
}
