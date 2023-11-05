package de.marshal.bankapp.repository;

import de.marshal.bankapp.entity.Agreement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface AgreementRepository extends Repository<Agreement, Long> {
    void save(Agreement agreement);

    Optional<Agreement> findById(Long id);

    @Query("""
                SELECT
                    agreement,
                    product,
                    account,
                    client
                FROM
                    Agreement agreement,
                    Product product,
                    Account account,
                    Client client
                WHERE 1=1
                    AND agreement.product.id = product.id
                    AND agreement.account.id = account.id
                    AND account.client.id = client.id
                    AND client.id = ?1
            """)
    List<Agreement> findByClientId(Long clientId);
}
