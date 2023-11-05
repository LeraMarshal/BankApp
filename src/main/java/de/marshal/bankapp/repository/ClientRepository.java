package de.marshal.bankapp.repository;

import de.marshal.bankapp.entity.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ClientRepository extends Repository<Client, Long> {
    void save(Client client);

    Optional<Client> findById(Long id);
    Optional<Client> findByEmail(String email);
    Optional<Client> findByPhone(String phone);

    @Query("SELECT c FROM Client c JOIN FETCH c.accounts WHERE c.id = ?1")
    Optional<Client> findByIdWithAccounts(Long id);
}
