package de.marshal.bankapp.repository;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.entity.Agreement;
import de.marshal.bankapp.entity.AgreementStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DirtiesContext // пересоздать контекст после завершения тестов в классе
public class AgreementRepositoryITest extends AppITests {
    @Autowired
    AgreementRepository agreementRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    void countTest() {
        Assertions.assertEquals(2, agreementRepository.count());
    }

    @Test
    @Transactional
        // откатывает изменения, сделанные в рамках теста
    void saveTest() {
        Agreement agreement = new Agreement(
                accountRepository.findById(1L).orElseThrow(),
                productRepository.findById(1L).orElseThrow(),
                AgreementStatus.ACTIVE,
                10,
                80000L
        );

        agreementRepository.save(agreement);

        Assertions.assertEquals(3, agreementRepository.count());
        Assertions.assertEquals(3, agreement.getId());
        Assertions.assertNotNull(agreement.getCreatedAt());
        Assertions.assertNotNull(agreement.getUpdatedAt());
    }

    @Test
    void findByIdTest() {
        Assertions.assertNotEquals(Optional.empty(), agreementRepository.findById(1L));
        Assertions.assertEquals(Optional.empty(), agreementRepository.findById(0L));
    }

    @Test
    void linkedAccountAndProductTest(){
        Agreement agreement = agreementRepository.findById(1L).orElseThrow();

        Assertions.assertEquals(1, agreement.getProduct().getId());
        Assertions.assertEquals(1, agreement.getAccount().getId());
    }

    @Test
    @Transactional
    void findByClientIdTest() {
        Assertions.assertEquals(1, agreementRepository
                .findByClientId(1L)
                .size());
    }
}
