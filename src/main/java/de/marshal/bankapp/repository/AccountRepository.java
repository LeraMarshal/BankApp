package de.marshal.bankapp.repository;

import de.marshal.bankapp.entity.Account;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface AccountRepository extends Repository<Account, Long> {
    long count();

    void save(Account account);

    Optional<Account> findById(Long id);
}
