package de.marshal.bankapp.repository;

import de.marshal.bankapp.entity.Client;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ClientRepository extends Repository<Client, Long> {
    long count();

    void save(Client client);

    Optional<Client> findByEmail(String email);
    Optional<Client> findByPhone(String phone);
}
