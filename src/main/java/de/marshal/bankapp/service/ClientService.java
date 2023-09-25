package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.Client;
import de.marshal.bankapp.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClientService {
    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientByEmail(String email){
        return clientRepository.findByEmail(email).orElse(null);
    }

    public Client getClientByPhone(String phone){
        return clientRepository.findByPhone(phone).orElse(null);
    }}
