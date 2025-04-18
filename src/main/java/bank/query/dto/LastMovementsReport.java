package bank.query.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LastMovementsReport {
	private List<TransactionDTO> debitCardMovements;
    private List<TransactionDTO> creditCardMovements;
}
