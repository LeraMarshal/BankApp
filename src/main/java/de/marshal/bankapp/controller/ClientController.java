package de.marshal.bankapp.controller;

import de.marshal.bankapp.dto.ClientDTO;
import de.marshal.bankapp.entity.Client;
import de.marshal.bankapp.mapper.ClientMapper;
import de.marshal.bankapp.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ClientController {
    private ClientService clientService;
    private ClientMapper clientMapper;

    @Autowired
    public ClientController(ClientService clientService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @GetMapping("/client/search")
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

        return ResponseEntity.ok(clientMapper.toDTO(client));
    }
}
