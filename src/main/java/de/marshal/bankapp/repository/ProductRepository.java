package de.marshal.bankapp.repository;

import de.marshal.bankapp.entity.Product;
import de.marshal.bankapp.entity.ProductStatus;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends Repository<Product, Long> {
    void save(Product product);

    Optional<Product> findById(Long Id);

    List<Product> findAllByStatus(ProductStatus status);
}
