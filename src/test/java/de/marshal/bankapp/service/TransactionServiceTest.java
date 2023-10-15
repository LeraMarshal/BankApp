package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.Account;
import de.marshal.bankapp.entity.AccountStatus;
import de.marshal.bankapp.entity.Client;
import de.marshal.bankapp.entity.Transaction;
import de.marshal.bankapp.exception.AccountNotFoundException;
import de.marshal.bankapp.exception.CurrencyCodeMismatchException;
import de.marshal.bankapp.exception.InsufficientFundsException;
import de.marshal.bankapp.repository.AccountRepository;
import de.marshal.bankapp.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {
    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private AccountRepository accountRepository;

    private TransactionService transactionService;

    private Account account1;
    private Account account2;

    @BeforeEach
    public void setup() {
        transactionService = new TransactionService(transactionRepository, accountRepository);

        account1 = new Account(
                new Client(),
                "Account 1",
                AccountStatus.ACTIVE,
                10000L,
                987
        );

        account2 = new Account(
                new Client(),
                "Account 2",
                AccountStatus.ACTIVE,
                0L,
                987
        );
    }

    @Test
    public void findByClientIdTest() {
        Mockito.when(transactionRepository.findByAccountId(1L)).thenReturn(List.of(new Transaction()));
        assertEquals(1, transactionService.findByAccountId(1L).size());
        Mockito.verify(transactionRepository).findByAccountId(1L);
    }

    @Test
    public void createSuccessTest() throws Exception {
        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        Mockito.doNothing().when(transactionRepository).save(transactionCaptor.capture());

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        Mockito.doNothing().when(accountRepository).save(accountCaptor.capture());

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        Mockito.when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

        Transaction transaction = transactionService.create(1L, 2L, 10000L, "Test");
        assertEquals(transaction, transactionCaptor.getValue());

        assertTrue(accountCaptor.getAllValues().contains(account1));
        assertTrue(accountCaptor.getAllValues().contains(account2));

        assertEquals(account1.getBalance(), 0L);
        assertEquals(account2.getBalance(), 10000L);
    }

    @Test
    public void createInsufficientFundsTest() {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));

        assertThrows(InsufficientFundsException.class, () -> {
            transactionService.create(1L, 2L, 50000L, "test");
        });
    }

    @Test
    public void createCurrencyCodeMismatchTest() {
        account2.setCurrencyCode(100);

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        Mockito.when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

        assertThrows(CurrencyCodeMismatchException.class, () -> {
            transactionService.create(1L, 2L, 10000L, "test");
        });
    }

    @Test
    public void createAccountNotFoundExceptionTest() {
        account2.setBalance(10000L);

        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.create(1L, 2L, 10000L, "test");
        });

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.create(2L, 1L, 10000L, "test");
        });
    }
}
