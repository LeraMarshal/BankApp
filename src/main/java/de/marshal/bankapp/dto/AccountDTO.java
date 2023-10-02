package de.marshal.bankapp.dto;

import de.marshal.bankapp.entity.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private Long id;
    private String name;
    private AccountStatus status;
    private Long balance;
    private Integer currencyCode;
}
