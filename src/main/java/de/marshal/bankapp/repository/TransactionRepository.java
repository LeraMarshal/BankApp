package de.marshal.bankapp.repository;

import de.marshal.bankapp.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends Repository<Transaction, Long> {
    long count();

    void save(Transaction transaction);

    Optional<Transaction> findById(Long id);

    @Query("""
                SELECT
                    transaction,
                    creditAccount,
                    debitAccount
                FROM
                    Transaction transaction
                JOIN
                    Account creditAccount ON creditAccount.id = transaction.creditAccount.id
                JOIN
                    Account debitAccount ON debitAccount.id = transaction.debitAccount.id
                WHERE 1=0
                    OR creditAccount.id = ?1
                    OR debitAccount.id = ?1
            """)
    List<Transaction> findByAccountId(Long accountId);
}
