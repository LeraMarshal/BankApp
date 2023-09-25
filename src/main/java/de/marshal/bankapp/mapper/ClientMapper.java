package de.marshal.bankapp.mapper;

import de.marshal.bankapp.dto.ClientDTO;
import de.marshal.bankapp.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {
    public ClientDTO toDTO(Client client){
        if (client == null){
            return null;
        }

        return new ClientDTO(
                client.getId(),
                client.getStatus(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone()
        );
    }
}
