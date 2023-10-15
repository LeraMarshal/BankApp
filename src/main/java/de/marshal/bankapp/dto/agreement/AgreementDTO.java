package de.marshal.bankapp.dto.agreement;

import de.marshal.bankapp.entity.AgreementStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementDTO {
    private long id;
    private int productId;
    private AgreementStatus status;
    private int interestRate;
    private long debt;
    private Timestamp createdAt;
}
