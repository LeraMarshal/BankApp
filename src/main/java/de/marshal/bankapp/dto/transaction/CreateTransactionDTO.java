package de.marshal.bankapp.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionDTO {
    private long debitAccountId;
    private long creditAccountId;
    private long amount;
    private String description;
}
