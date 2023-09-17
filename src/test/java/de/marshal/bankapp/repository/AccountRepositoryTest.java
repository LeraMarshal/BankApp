package de.marshal.bankapp.repository;

import de.marshal.bankapp.AppTests;
import de.marshal.bankapp.entity.Account;
import de.marshal.bankapp.entity.AccountStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DirtiesContext // пересоздать контекст после завершения тестов в классе
public class AccountRepositoryTest extends AppTests {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Test
    void countTest() {
        Assertions.assertEquals(2, accountRepository.count());
    }

    @Test
    @Transactional
        // откатывает изменения, сделанные в рамках теста
    void saveTest() {
        Account account = new Account(
                clientRepository.findByEmail("john.smith@gmail.com").orElseThrow(),
                "debit",
                AccountStatus.ACTIVE,
                300000L,
                978
        );

        accountRepository.save(account);

        Assertions.assertEquals(3, accountRepository.count());
        Assertions.assertEquals(3, account.getId());
        Assertions.assertNotNull(account.getCreatedAt());
        Assertions.assertNotNull(account.getUpdatedAt());
    }

    @Test
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
}
