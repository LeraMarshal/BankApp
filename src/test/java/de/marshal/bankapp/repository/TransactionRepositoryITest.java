package de.marshal.bankapp.repository;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.entity.Transaction;
import de.marshal.bankapp.entity.TransactionStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DirtiesContext
public class TransactionRepositoryITest extends AppITests {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Test
    void countTest(){
        Assertions.assertEquals(2, transactionRepository.count());
    }

    @Test
    @Transactional
    void saveTest(){
        Transaction transaction = new Transaction(
                accountRepository.findById(1L).orElseThrow(),
                accountRepository.findById(2L).orElseThrow(),
                TransactionStatus.COMPLETED,
                9000L,
                "gift"
        );

        transactionRepository.save(transaction);

        Assertions.assertEquals(3, transactionRepository.count());
        Assertions.assertEquals(3, transaction.getId());
        Assertions.assertNotNull(transaction.getCreatedAt());
        Assertions.assertNotNull(transaction.getUpdatedAt());
    }

    @Test
    void findByIdTest() {
        Assertions.assertNotEquals(Optional.empty(), transactionRepository.findById(1L));
        Assertions.assertEquals(Optional.empty(), transactionRepository.findById(0L));
    }

    @Test
    void linkedAccountsTest(){
        Transaction transaction = transactionRepository.findById(1L).orElseThrow();

        Assertions.assertEquals(1, transaction.getDebitAccount().getId());
        Assertions.assertEquals(2, transaction.getCreditAccount().getId());
    }
}
