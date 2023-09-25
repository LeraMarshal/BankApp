package de.marshal.bankapp.dto;

import de.marshal.bankapp.entity.ClientStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ClientDTO {
    private Long id;
    private ClientStatus status;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
}
