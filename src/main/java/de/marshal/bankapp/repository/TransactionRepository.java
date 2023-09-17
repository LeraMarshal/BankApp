package de.marshal.bankapp.repository;

import de.marshal.bankapp.entity.Transaction;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface TransactionRepository extends Repository<Transaction, Long> {
    long count();

    void save(Transaction transaction);

    Optional<Transaction> findById(Long id);
}
