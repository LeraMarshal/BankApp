package de.marshal.bankapp.mapper;

import de.marshal.bankapp.dto.client.ClientDTO;
import de.marshal.bankapp.dto.client.ClientWithAccountsDTO;
import de.marshal.bankapp.dto.client.RegisterClientDTO;
import de.marshal.bankapp.entity.Client;
import org.mapstruct.Mapper;

//@Component
@Mapper(componentModel = "spring", uses = {AccountMapper.class})
public interface ClientMapper {
    ClientDTO clientToClientDTO(Client client);
    ClientWithAccountsDTO clientToClientWithAccountsDTO(Client client);

    Client registerClientDTOToClient(RegisterClientDTO registerClientDTO);
}
