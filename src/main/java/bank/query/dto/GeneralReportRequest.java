package bank.query.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralReportRequest {
	private String productType; // ACCOUNT, CREDIT_CARD, LOAN
    private LocalDate startDate;
    private LocalDate endDate;
}
