package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.*;
import de.marshal.bankapp.exception.ProductNotFoundException;
import de.marshal.bankapp.repository.ProductRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public Product getById(long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product with id " + id + " not found"));
    }

    public List<Product> findActive() {
        return productRepository.findAllByStatus(ProductStatus.ACTIVE);
    }

    public Product create(
            @NonNull String name,
            int currencyCode,
            int minInterestRate,
            long maxOfferLimit
    ) {
        log.info("Creating new product, name=[{}], currencyCode=[{}], minInterestRate=[{}], maxOfferLimit=[{}]",
                name, currencyCode, minInterestRate, maxOfferLimit);

        Product product = new Product(name, ProductStatus.ACTIVE, currencyCode, minInterestRate, maxOfferLimit);

        productRepository.save(product);

        return product;
    }

    public Product delete(long id) throws ProductNotFoundException {
        Product product = getById(id);

        log.info("Deleting product, id=[{}], name=[{}]", id, product.getName());

        product.setStatus(ProductStatus.INACTIVE);

        productRepository.save(product);

        return product;
    }
}
