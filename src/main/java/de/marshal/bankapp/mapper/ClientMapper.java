package de.marshal.bankapp.mapper;

import de.marshal.bankapp.dto.ClientDTO;
import de.marshal.bankapp.dto.ClientWithAccountsDTO;
import de.marshal.bankapp.dto.RegisterClientDTO;
import de.marshal.bankapp.entity.Client;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

//@Component
@Mapper(componentModel = "spring", uses = {AccountMapper.class})
public interface ClientMapper {
    ClientDTO clientToClientDTO(Client client);
    ClientWithAccountsDTO clientToClientWithAccountsDTO(Client client);

    Client registerClientDTOToClient(RegisterClientDTO registerClientDTO);
}
