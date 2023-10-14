package de.marshal.bankapp.controller;

import de.marshal.bankapp.dto.ClientDTO;
import de.marshal.bankapp.dto.ClientWithAccountsDTO;
import de.marshal.bankapp.dto.RegisterClientDTO;
import de.marshal.bankapp.entity.Client;
import de.marshal.bankapp.mapper.ClientMapper;
import de.marshal.bankapp.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientController {
    private final ClientService clientService;
    private final ClientMapper clientMapper;

    @GetMapping("/client")
    public ResponseEntity<ClientDTO> search(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email
    ) {
        log.debug("Phone: {}, Email: {}", phone, email);

        Client client;

        if ((phone != null && email != null) || (phone == null && email == null)) {
            return ResponseEntity.badRequest().build();
        } else if (phone != null) {
            client = clientService.getClientByPhone(phone);
        } else {
            client = clientService.getClientByEmail(email);
        }

        if (client == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clientMapper.clientToClientDTO(client));
    }

    @GetMapping("/client/{id}")
    @Transactional
    public ResponseEntity<ClientWithAccountsDTO> getById(@PathVariable long id) {
        Client client = clientService.getClientById(id);

        if (client == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clientMapper.clientToClientWithAccountsDTO(client));
    }

    @PutMapping("/client")
    @Transactional
    public ResponseEntity<ClientWithAccountsDTO> register(@RequestBody RegisterClientDTO registerClientDTO) {
        Client client = clientMapper.registerClientDTOToClient(registerClientDTO);

        clientService.register(client);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clientMapper.clientToClientWithAccountsDTO(client));
    }
}
