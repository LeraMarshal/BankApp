package de.marshal.bankapp.dto;

import de.marshal.bankapp.entity.ClientStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientWithAccountsDTO {
    private Long id;
    private ClientStatus status;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private List<AccountDTO> accounts;
}
