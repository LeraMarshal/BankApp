package de.marshal.bankapp.controller;

import de.marshal.bankapp.dto.client.ClientDTO;
import de.marshal.bankapp.dto.client.ClientWithAccountsDTO;
import de.marshal.bankapp.dto.client.RegisterClientDTO;
import de.marshal.bankapp.entity.Client;
import de.marshal.bankapp.exception.ClientNotFoundException;
import de.marshal.bankapp.exception.ProductNotFoundException;
import de.marshal.bankapp.mapper.ClientMapper;
import de.marshal.bankapp.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @GetMapping
    public ResponseEntity<ClientDTO> search(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email
    ) {
        log.debug("Phone: {}, Email: {}", phone, email);

        Client client;

        if ((phone != null && email != null) || (phone == null && email == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot search by both email and phone", null);
        } else if (phone != null) {
            client = clientService.getClientByPhone(phone);
        } else {
            client = clientService.getClientByEmail(email);
        }

        if (client == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, null, new ClientNotFoundException());
        }

        return ResponseEntity.ok(clientMapper.clientToClientDTO(client));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<ClientWithAccountsDTO> getById(@PathVariable long id) {
        Client client = clientService.getClientById(id);

        if (client == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clientMapper.clientToClientWithAccountsDTO(client));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<ClientWithAccountsDTO> register(@RequestBody RegisterClientDTO registerClientDTO) {
        try {
            Client client = clientService.register(
                    registerClientDTO.getFirstName(),
                    registerClientDTO.getLastName(),
                    registerClientDTO.getEmail(),
                    registerClientDTO.getAddress(),
                    registerClientDTO.getPhone()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(clientMapper.clientToClientWithAccountsDTO(client));
        } catch (ProductNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, null, ex);
        }
    }
}
