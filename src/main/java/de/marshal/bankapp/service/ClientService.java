package de.marshal.bankapp.service;

import de.marshal.bankapp.Constants;
import de.marshal.bankapp.entity.*;
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

    public Client getClientById(long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email).orElse(null);
    }

    public Client getClientByPhone(String phone) {
        return clientRepository.findByPhone(phone).orElse(null);
    }

    public void register(Client client) {
        Product defaultProduct = productRepository.findById(Constants.DEFAULT_PRODUCT_ID).orElseThrow();

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

        client.setStatus(ClientStatus.ACTIVE);
        client.setAccounts(List.of(account));

        account.setAgreements(List.of(agreement));

        clientRepository.save(client);
    }
}
