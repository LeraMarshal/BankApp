package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.*;
import de.marshal.bankapp.exception.ClientNotFoundException;
import de.marshal.bankapp.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;

    public Client getById(long id) throws ClientNotFoundException {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("client with id " + id + " not found"));
    }

    public Client getByIdWithAccounts(long id) throws ClientNotFoundException {
        return clientRepository.findByIdWithAccounts(id)
                .orElseThrow(() -> new ClientNotFoundException("client with id " + id + " not found"));
    }

    public Client getByEmail(String email) throws ClientNotFoundException {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("client with email " + email + " not found"));
    }

    public Client getByPhone(String phone) throws ClientNotFoundException {
        return clientRepository.findByPhone(phone)
                .orElseThrow(() -> new ClientNotFoundException("client with phone " + phone + " not found"));
    }
}
