package de.marshal.bankapp.repository;

import de.marshal.bankapp.entity.Product;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ProductRepository extends Repository<Product, Long> {
    void save(Product product);

    Optional<Product> findById(Long Id);
}
