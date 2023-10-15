package de.marshal.bankapp.repository;

import de.marshal.bankapp.entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends Repository<Account, Long> {
    long count();

    void save(Account account);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findById(Long id);

    @Query("SELECT account,client FROM Account account JOIN Client client ON client.id = account.client.id WHERE client.id = ?1")
    List<Account> findByClientId(Long clientId);
}
