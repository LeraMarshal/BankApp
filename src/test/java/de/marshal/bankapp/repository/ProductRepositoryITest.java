package de.marshal.bankapp.repository;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.entity.Product;
import de.marshal.bankapp.entity.ProductStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DirtiesContext // пересоздать контекст после завершения тестов в классе
public class ProductRepositoryITest extends AppITests {
    @Autowired
    ProductRepository productRepository;

    @Test
    void countTest() {
        Assertions.assertEquals(3, productRepository.count());
    }

    @Test
    @Transactional
        // откатывает изменения, сделанные в рамках теста
    void saveTest() {
        Product product = new Product(
                "test product",
                ProductStatus.ACTIVE,
                978,
                15,
                130000L
        );

        productRepository.save(product);

        Assertions.assertEquals(4, productRepository.count());
        Assertions.assertEquals(3, product.getId());
        Assertions.assertNotNull(product.getCreatedAt());
        Assertions.assertNotNull(product.getUpdatedAt());
    }

    @Test
    void findByIdTest() {
        Assertions.assertNotEquals(Optional.empty(), productRepository.findById(1L));
        Assertions.assertEquals(Optional.empty(), productRepository.findById(999L));
    }

    @Test
    @Transactional
        // чтобы работал Lazy
    void linkedAgreementsTest() {
        Assertions.assertEquals(1, productRepository
                .findById(1L)
                .orElseThrow()
                .getAgreements()
                .size());
    }
}
