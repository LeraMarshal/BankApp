package de.marshal.bankapp.dto.agreement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAgreementDTO {
    private long accountId;
    private long productId;
    private int interestRate;
    private long amount;
}
