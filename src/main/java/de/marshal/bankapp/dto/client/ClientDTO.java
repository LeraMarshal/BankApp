package de.marshal.bankapp.dto.client;

import de.marshal.bankapp.entity.ClientStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private long id;
    private ClientStatus status;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
}
