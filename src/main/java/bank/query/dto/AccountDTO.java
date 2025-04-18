package bank.query.dto;


import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AccountDTO {
	private String accountNumber;
    private String documentNumber;
    private String type;
    private BigDecimal balance;
    
    // Asegúrate de que Jackson pueda deserializar la clase correctamente
    @JsonCreator
    public AccountDTO(
            @JsonProperty("accountNumber") String accountNumber,
            @JsonProperty("documentNumber") String documentNumber,
            @JsonProperty("type") String type,
            @JsonProperty("balance") BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.documentNumber = documentNumber;
        this.type = type;
        this.balance = balance;
    }
    
}
