package de.marshal.bankapp.repository;

import de.marshal.bankapp.entity.Agreement;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface AgreementRepository extends Repository<Agreement, Long> {
    long count();

    void save(Agreement agreement);

    Optional<Agreement> findById(Long id);
}
