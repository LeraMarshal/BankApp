package de.marshal.bankapp.service;

import de.marshal.bankapp.configuration.BankConfigurationProperties;
import de.marshal.bankapp.entity.*;
import de.marshal.bankapp.exception.*;
import de.marshal.bankapp.repository.ClientRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientRegistrationService {
    private final ClientRepository clientRepository;
    private final ProductService productService;
    private final AccountService accountService;
    private final AgreementService agreementService;
    private final BankConfigurationProperties bankConfigurationProperties;

    @Transactional
    public Client register(
            @NonNull String firstName,
            @NonNull String lastName,
            @NonNull String email,
            @NonNull String address,
            @NonNull String phone,
            int currencyCode
    ) throws ProductNotFoundException, InvalidStatusException, InvalidAmountException, InvalidInterestRateException {
        long defaultProductId = bankConfigurationProperties.defaultProductIdForCurrency(currencyCode);
        Product defaultProduct = productService.getById(defaultProductId);

        log.info("Creating new client, firstName=[{}], lastName=[{}], email=[{}], address=[{}], phone=[{}]",
                firstName, lastName, email, address, phone);

        Client client = new Client(
                ClientStatus.ACTIVE,
                firstName,
                lastName,
                email,
                address,
                phone
        );

        clientRepository.save(client);

        Account account = accountService.create(
                client,
                defaultProduct.getName(),
                defaultProduct.getCurrencyCode()
        );

        Agreement agreement = agreementService.create(account, defaultProduct);

        account.setAgreements(List.of(agreement));
        client.setAccounts(List.of(account));

        return client;
    }
}
