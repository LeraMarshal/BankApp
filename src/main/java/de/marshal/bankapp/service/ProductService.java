package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.*;
import de.marshal.bankapp.exception.ProductNotFoundException;
import de.marshal.bankapp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public Product getById(long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product with id " + id + " not found"));
    }
}
