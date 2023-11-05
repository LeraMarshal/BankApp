package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.*;
import de.marshal.bankapp.exception.*;
import de.marshal.bankapp.repository.AccountRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final ClientService clientService;

    public List<Account> findByClientId(long clientId) {
        return accountRepository.findByClientId(clientId);
    }

    @Transactional
    public Account getById(long accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("account with id " + accountId + " not found"));
    }

    @Transactional
    public Account deleteById(long accountId) throws AccountNotFoundException, InvalidStatusException,
            UnfulfilledObligationsException {
        Account account = getById(accountId);

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidStatusException("account status must be ACTIVE for deletion");
        }

        if (account.getBalance() != 0) {
            throw new UnfulfilledObligationsException("account must have zero balance for deletion");
        }

        long agreementsCount = account.getAgreements().stream()
                .filter(it -> it.getStatus() != AgreementStatus.INACTIVE)
                .count();
        if (agreementsCount > 0) {
            throw new UnfulfilledObligationsException(agreementsCount + " agreements found on account");
        }

        log.info("Deleting account, clientId=[{}], accountId=[{}]", account.getClient().getId(), account.getId());

        account.setStatus(AccountStatus.DELETED);

        accountRepository.save(account);

        return account;
    }

    @Transactional
    public Account create(long clientId, @NonNull String name, int currencyCode) throws ClientNotFoundException {
        Client client = clientService.getById(clientId);

        return create(client, name, currencyCode);
    }

    @Transactional
    public Account create(@NonNull Client client, @NonNull String name, int currencyCode) {
        log.info("Creating new account, clientId=[{}], name=[{}], currencyCode=[{}]", client.getId(), name, currencyCode);

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

    @Transactional(propagation = Propagation.MANDATORY)
    public void deposit(@NonNull Account account, long amount) throws InvalidStatusException {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidStatusException("account must be ACTIVE to deposit funds");
        }

        long balance = account.getBalance();

        log.info("Depositing funds to account, id=[{}], balance=[{}], amount=[{}]", account.getId(), balance, amount);

        account.setBalance(balance + amount);

        accountRepository.save(account);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void withdraw(@NonNull Account account, long amount) throws InvalidStatusException, InsufficientFundsException {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new InvalidStatusException("account must be ACTIVE to withdraw funds");
        }

        long balance = account.getBalance();
        if (balance < amount) {
            throw new InsufficientFundsException("not enough funds " + balance + " available when " + amount + " required");
        }

        log.info("Withdrawing funds from account, id=[{}], balance=[{}], amount=[{}]", account.getId(), balance, amount);

        account.setBalance(balance - amount);

        accountRepository.save(account);
    }
}
