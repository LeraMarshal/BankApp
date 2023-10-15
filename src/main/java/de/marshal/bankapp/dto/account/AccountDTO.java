package de.marshal.bankapp.dto.account;

import de.marshal.bankapp.entity.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private long id;
    private String name;
    private AccountStatus status;
    private long balance;
    private int currencyCode;
}
