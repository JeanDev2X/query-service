package bank.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
	private String id;
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String customerType; // PERSONAL, BUSINESS
}
