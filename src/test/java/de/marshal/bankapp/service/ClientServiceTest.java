package de.marshal.bankapp.service;

import de.marshal.bankapp.Constants;
import de.marshal.bankapp.entity.*;
import de.marshal.bankapp.repository.AgreementRepository;
import de.marshal.bankapp.repository.ClientRepository;
import de.marshal.bankapp.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ClientServiceTest {
    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private ProductRepository productRepository;

    private ClientService clientService;


    @BeforeEach
    public void setup() {
        clientService = new ClientService(clientRepository, productRepository);
    }

    @Test
    public void getClientByIdTestExists() {
        Mockito.when(clientRepository.findById(1L)).thenReturn(Optional.of(new Client()));
        Assertions.assertNotNull(clientService.getClientById(1L));
        Mockito.verify(clientRepository).findById(1L);
    }

    @Test
    public void getClientByIdTestNotExists() {
        Mockito.when(clientRepository.findById(0L)).thenReturn(Optional.empty());
        Assertions.assertNull(clientService.getClientById(0L));
        Mockito.verify(clientRepository).findById(0L);
    }

    @Test
    public void getClientByEmailTestExists() {
        Mockito.when(clientRepository.findByEmail("abc")).thenReturn(Optional.of(new Client()));
        Assertions.assertNotNull(clientService.getClientByEmail("abc"));
        Mockito.verify(clientRepository).findByEmail("abc");
    }

    @Test
    public void getClientByEmailTestNotExists() {
        Mockito.when(clientRepository.findByEmail("abc")).thenReturn(Optional.empty());
        Assertions.assertNull(clientService.getClientByEmail("abc"));
        Mockito.verify(clientRepository).findByEmail("abc");
    }

    @Test
    public void getClientByPhoneTestExists() {
        Mockito.when(clientRepository.findByPhone("abc")).thenReturn(Optional.of(new Client()));
        Assertions.assertNotNull(clientService.getClientByPhone("abc"));
        Mockito.verify(clientRepository).findByPhone("abc");
    }

    @Test
    public void getClientByPhoneTestNotExists() {
        Mockito.when(clientRepository.findByPhone("abc")).thenReturn(Optional.empty());
        Assertions.assertNull(clientService.getClientByPhone("abc"));
        Mockito.verify(clientRepository).findByPhone("abc");
    }

    @Test
    public void registerTest() {
        Product defaultProduct = new Product(
                Constants.DEFAULT_PRODUCT_ID,
                "default product",
                ProductStatus.ACTIVE,
                978,
                0,
                0L,
                Timestamp.from(Instant.now()),
                Timestamp.from(Instant.now()),
                Collections.emptyList()
        );

        Mockito.when(productRepository.findById(Constants.DEFAULT_PRODUCT_ID)).thenReturn(Optional.of(defaultProduct));

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        Mockito.doNothing().when(clientRepository).save(clientCaptor.capture());

        clientService.register(
                new Client(
                        ClientStatus.INACTIVE,
                        "Vasilii",
                        "Rio",
                        "vasiario@gmail.com",
                        "Germany, Berlin",
                        "49100100100"
                )
        );

        Client client = clientCaptor.getValue();

        Assertions.assertEquals(ClientStatus.ACTIVE, client.getStatus());
        Assertions.assertEquals(1, client.getAccounts().size());

        Account account = client.getAccounts().get(0);

        Assertions.assertEquals(client, account.getClient());
        Assertions.assertEquals(Constants.DEFAULT_ACCOUNT_NAME, account.getName());
        Assertions.assertEquals(AccountStatus.ACTIVE, account.getStatus());
        Assertions.assertEquals(0L, account.getBalance());
        Assertions.assertEquals(978, account.getCurrencyCode());
        Assertions.assertEquals(1, account.getAgreements().size());

        Agreement agreement = account.getAgreements().get(0);

        Assertions.assertEquals(account, agreement.getAccount());
        Assertions.assertEquals(defaultProduct, agreement.getProduct());
        Assertions.assertEquals(AgreementStatus.ACTIVE, agreement.getStatus());
        Assertions.assertEquals(0, agreement.getInterestRate());
        Assertions.assertEquals(0L, agreement.getDebt());
    }
}
