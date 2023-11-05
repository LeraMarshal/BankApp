package de.marshal.bankapp.repository;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.entity.Account;
import de.marshal.bankapp.entity.AccountStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DirtiesContext
public class AccountRepositoryITest extends AppITests {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Test
    @Transactional
    void saveTest() {
        Account account = new Account(
                clientRepository.findByEmail("john.smith@gmail.com").orElseThrow(),
                "debit",
                AccountStatus.ACTIVE,
                300000L,
                978
        );

        accountRepository.save(account);

        Assertions.assertEquals(accountRepository.findById(account.getId()).orElse(null), account);

        Assertions.assertNotNull(account.getCreatedAt());
        Assertions.assertNotNull(account.getUpdatedAt());
    }

    @Test
    @Transactional
    void findByIdTest() {
        Assertions.assertNotEquals(Optional.empty(), accountRepository.findById(1L));
        Assertions.assertEquals(Optional.empty(), accountRepository.findById(0L));
    }

    @Test
    @Transactional
        // чтобы работал Lazy
    void linkedAgreementsTest() {
        Assertions.assertEquals(1, accountRepository
                .findById(1L)
                .orElseThrow()
                .getAgreements()
                .size());
    }

    @Test
    @Transactional
        // чтобы работал Lazy
    void linkedDebitTransactionsTest() {
        Assertions.assertEquals(1, accountRepository
                .findById(1L)
                .orElseThrow()
                .getDebitTransactions()
                .size());
    }

    @Test
    @Transactional
        // чтобы работал Lazy
    void linkedCreditTransactionsTest() {
        Assertions.assertEquals(1, accountRepository
                .findById(1L)
                .orElseThrow()
                .getCreditTransactions()
                .size());
    }

    @Test
    @Transactional
    void findByClientIdTest() {
        Assertions.assertEquals(1, accountRepository
                .findByClientId(1L)
                .size());
    }
}
