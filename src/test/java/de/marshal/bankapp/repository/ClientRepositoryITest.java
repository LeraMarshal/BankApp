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

@DirtiesContext // пересоздать контекст после завершения тестов в классе
public class ClientRepositoryITest extends AppITests {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    void countTest() {
        Assertions.assertEquals(2, clientRepository.count());
    }

    @Test
    @Transactional
        // откатывает изменения, сделанные в рамках теста
    void saveTest() {
        Client client = new Client(
                ClientStatus.ACTIVE,
                "Name1",
                "LName1",
                "email1",
                "Pass1",
                "Address1",
                "000"
        );

        clientRepository.save(client);

        Assertions.assertEquals(3, clientRepository.count());
        Assertions.assertEquals(3, client.getId());
        Assertions.assertNotNull(client.getCreatedAt());
        Assertions.assertNotNull(client.getUpdatedAt());
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
        // чтобы работал Lazy
    void linkedAccountsTest() { // тестируем, что связанные аккаунты достаются из базы
        Assertions.assertEquals(1, clientRepository
                .findByEmail("john.smith@gmail.com")
                .orElseThrow()
                .getAccounts()
                .size());
    }
}
