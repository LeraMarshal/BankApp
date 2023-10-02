package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.Client;
import de.marshal.bankapp.repository.ClientRepository;
import de.marshal.bankapp.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
}
