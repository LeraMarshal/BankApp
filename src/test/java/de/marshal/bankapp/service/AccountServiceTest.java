package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.*;
import de.marshal.bankapp.repository.AccountRepository;
import de.marshal.bankapp.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {
    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private ClientRepository clientRepository;

    private AccountService accountService;

    @BeforeEach
    public void setup() {
        accountService = new AccountService(accountRepository, clientRepository);
    }

    @Test
    public void findByClientIdTest() throws Exception {
        Mockito.when(accountRepository.findByClientId(1L)).thenReturn(List.of(new Account()));
        Assertions.assertEquals(1, accountService.findByClientId(1L).size());
        Mockito.verify(accountRepository).findByClientId(1L);
    }

    @Test
    public void createTest() throws Exception {
        Client client = new Client();
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        Mockito.doNothing().when(accountRepository).save(accountCaptor.capture());

        accountService.create(1L, "test", 919);

        Account account = accountCaptor.getValue();

        Assertions.assertNull(account.getId());
        Assertions.assertEquals(client, account.getClient());
        Assertions.assertEquals(AccountStatus.ACTIVE, account.getStatus());
        Assertions.assertEquals(0L, account.getBalance());
        Assertions.assertEquals(919, account.getCurrencyCode());

        Mockito.verify(clientRepository).findById(1L);
    }
}
