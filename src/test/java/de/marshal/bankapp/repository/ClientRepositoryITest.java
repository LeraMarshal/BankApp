package de.marshal.bankapp.repository;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.entity.Client;
import de.marshal.bankapp.entity.ClientStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DirtiesContext
public class ClientRepositoryITest extends AppITests {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @Transactional
    void saveTest() {
        Client client = new Client(
                ClientStatus.ACTIVE,
                "Name1",
                "LName1",
                "email1",
                "Address1",
                "000"
        );

        clientRepository.save(client);

        Assertions.assertEquals(clientRepository.findById(client.getId()).orElse(null), client);

        Assertions.assertNotNull(client.getCreatedAt());
        Assertions.assertNotNull(client.getUpdatedAt());
    }

    @Test
    void findByIdTest() {
        Assertions.assertNotEquals(Optional.empty(), clientRepository.findById(1L));
        Assertions.assertEquals(Optional.empty(), clientRepository.findById(0L));
    }

    @Test
    void findByEmailTest() {
        Assertions.assertNotEquals(Optional.empty(), clientRepository.findByEmail("john.smith@gmail.com"));
        Assertions.assertEquals(Optional.empty(), clientRepository.findByEmail("abc"));
    }

    @Test
    void findByPhoneTest() {
        Assertions.assertNotEquals(Optional.empty(), clientRepository.findByPhone("493048898888"));
        Assertions.assertEquals(Optional.empty(), clientRepository.findByPhone("abc"));
    }

    @Test
    @Transactional
    void linkedAccountsTest() { // тестируем, что связанные аккаунты достаются из базы
        Assertions.assertEquals(1, clientRepository
                .findByEmail("john.smith@gmail.com")
                .orElseThrow()
                .getAccounts()
                .size());
    }
}
