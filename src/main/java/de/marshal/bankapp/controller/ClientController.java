package de.marshal.bankapp.controller;

import de.marshal.bankapp.dto.ResponseDTO;
import de.marshal.bankapp.dto.client.ClientDTO;
import de.marshal.bankapp.dto.client.ClientWithAccountsDTO;
import de.marshal.bankapp.dto.client.RegisterClientDTO;
import de.marshal.bankapp.entity.Client;
import de.marshal.bankapp.exception.ApplicationException;
import de.marshal.bankapp.exception.IllegalSearchParamsException;
import de.marshal.bankapp.mapper.ClientMapper;
import de.marshal.bankapp.service.ClientRegistrationService;
import de.marshal.bankapp.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final ClientRegistrationService clientRegistrationService;
    private final ClientMapper clientMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<ClientDTO> search(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email
    ) throws ApplicationException {
        Client client;

        if ((phone != null && email != null) || (phone == null && email == null)) {
            throw new IllegalSearchParamsException("unable to search by both phone and email");
        } else if (phone != null) {
            client = clientService.getByPhone(phone);
        } else {
            client = clientService.getByEmail(email);
        }

        return ResponseDTO.ok(clientMapper.clientToClientDTO(client));
    }

    @GetMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<ClientWithAccountsDTO> getById(
            @PathVariable long id
    ) throws ApplicationException {
        Client client = clientService.getByIdWithAccounts(id);

        return ResponseDTO.ok(clientMapper.clientToClientWithAccountsDTO(client));
    }

    @PutMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO<ClientWithAccountsDTO> register(
            @RequestBody RegisterClientDTO registerClientDTO
    ) throws ApplicationException {
        Client client = clientRegistrationService.register(
                registerClientDTO.getFirstName(),
                registerClientDTO.getLastName(),
                registerClientDTO.getEmail(),
                registerClientDTO.getAddress(),
                registerClientDTO.getPhone(),
                registerClientDTO.getCurrencyCode()
        );

        return ResponseDTO.ok(clientMapper.clientToClientWithAccountsDTO(client));
    }
}
