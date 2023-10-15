package de.marshal.bankapp.dto.client;

import de.marshal.bankapp.dto.account.AccountDTO;
import de.marshal.bankapp.entity.ClientStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientWithAccountsDTO {
    private long id;
    private ClientStatus status;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private List<AccountDTO> accounts;
}
