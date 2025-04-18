package bank.query.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class TransactionDTO {
//	private String id;
    private String accountNumber;
    private String type;
    private BigDecimal amount;
    private LocalDateTime date;
    private BigDecimal commission;
    private String productType;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String cardNumber;
    
    @JsonCreator
    public TransactionDTO(
            @JsonProperty("accountNumber") String accountNumber,
            @JsonProperty("type") String type,
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("date") LocalDateTime date,
            @JsonProperty("commission") BigDecimal commission,
            @JsonProperty("productType") String productType,
            @JsonProperty("sourceAccountNumber") String sourceAccountNumber,
            @JsonProperty("destinationAccountNumber") String destinationAccountNumber,
            @JsonProperty("cardNumber") String cardNumber) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.commission = commission;
        this.productType = productType;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.cardNumber = cardNumber;
    }
    
}
