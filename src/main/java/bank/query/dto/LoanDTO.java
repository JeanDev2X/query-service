package bank.query.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanDTO {
	private String creditNumber;
	private BigDecimal amount;
    private String documentNumber;
    private BigDecimal balance;
    private String type;
    
    @JsonCreator
    public LoanDTO(
            @JsonProperty("creditNumber") String creditNumber,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("documentNumber") String documentNumber,
            @JsonProperty("balance") BigDecimal balance,
            @JsonProperty("type") String type) {
        this.creditNumber = creditNumber;
        this.amount = amount;
        this.documentNumber = documentNumber;
        this.balance = balance;
        this.type = type;
    }
    
}
