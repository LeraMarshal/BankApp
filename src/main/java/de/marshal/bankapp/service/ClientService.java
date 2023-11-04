package de.marshal.bankapp.service;

import de.marshal.bankapp.Constants;
import de.marshal.bankapp.entity.*;
import de.marshal.bankapp.exception.ApplicationException;
import de.marshal.bankapp.exception.ClientNotFoundException;
import de.marshal.bankapp.exception.ProductNotFoundException;
import de.marshal.bankapp.exception.UnspecifiedServerException;
import de.marshal.bankapp.repository.ClientRepository;
import de.marshal.bankapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    public Client getClientById(long id) throws ClientNotFoundException {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("client with id " + id + " not found"));
    }

    public Client getClientByEmail(String email) throws ClientNotFoundException {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("client with email " + email + " not found"));
    }

    public Client getClientByPhone(String phone) throws ClientNotFoundException {
        return clientRepository.findByPhone(phone)
                .orElseThrow(() -> new ClientNotFoundException("client with phone " + phone + " not found"));
    }

    public Client register(
            String firstName,
            String lastName,
            String email,
            String address,
            String phone
    ) throws UnspecifiedServerException {
        Product defaultProduct = productRepository.findById(Constants.DEFAULT_PRODUCT_ID)
                .orElseThrow(() -> new UnspecifiedServerException("default product not found"));

        Client client = new Client(
                ClientStatus.ACTIVE,
                firstName,
                lastName,
                email,
                address,
                phone
        );

        Account account = new Account(
                client,
                Constants.DEFAULT_ACCOUNT_NAME,
                AccountStatus.ACTIVE,
                0L,
                defaultProduct.getCurrencyCode()
        );

        Agreement agreement = new Agreement(
                account,
                defaultProduct,
                AgreementStatus.ACTIVE,
                0,
                0L
        );

        account.setAgreements(List.of(agreement));

        client.setAccounts(List.of(account));

        clientRepository.save(client);

        return client;
    }
}
