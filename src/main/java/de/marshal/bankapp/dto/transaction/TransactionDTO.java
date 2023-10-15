package de.marshal.bankapp.dto.transaction;

import de.marshal.bankapp.entity.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private long id;
    private long creditAccountId;
    private long debitAccountId;
    private TransactionStatus status;
    private long amount;
    private String description;
    private Timestamp createdAt;
}
