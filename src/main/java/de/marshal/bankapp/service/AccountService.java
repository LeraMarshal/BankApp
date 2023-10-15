package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.Account;
import de.marshal.bankapp.entity.AccountStatus;
import de.marshal.bankapp.entity.Client;
import de.marshal.bankapp.exception.ClientNotFoundException;
import de.marshal.bankapp.repository.AccountRepository;
import de.marshal.bankapp.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;

    public List<Account> findByClientId(long clientId) {
        return accountRepository.findByClientId(clientId);
    }

    public Account create(long clientId, String name, int currencyCode) throws ClientNotFoundException {
        Client client = clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);

        Account account = new Account(
                client,
                name,
                AccountStatus.ACTIVE,
                0L,
                currencyCode
        );

        accountRepository.save(account);

        return account;
    }
}
