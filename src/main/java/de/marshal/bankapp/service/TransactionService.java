package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.Account;
import de.marshal.bankapp.entity.Transaction;
import de.marshal.bankapp.entity.TransactionStatus;
import de.marshal.bankapp.exception.AccountNotFoundException;
import de.marshal.bankapp.exception.CurrencyCodeMismatchException;
import de.marshal.bankapp.exception.InsufficientFundsException;
import de.marshal.bankapp.exception.InvalidStatusException;
import de.marshal.bankapp.repository.TransactionRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public List<Transaction> findByAccountId(long accountId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt", "id"));
        return transactionRepository.findByAccountId(accountId, pageable);
    }

    @Transactional
    public Transaction create(
            long debitAccountId,
            long creditAccountId,
            @NonNull String description,
            long amount
    ) throws AccountNotFoundException, InsufficientFundsException, CurrencyCodeMismatchException, InvalidStatusException {
        Account debitAccount = accountService.getById(debitAccountId);
        Account creditAccount = accountService.getById(creditAccountId);

        return create(debitAccount, creditAccount, description, amount);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Transaction create(
            @NonNull Account debitAccount,
            @NonNull Account creditAccount,
            @NonNull String description,
            long amount
    ) throws InsufficientFundsException, InvalidStatusException, CurrencyCodeMismatchException {
        int debitCurrencyCode = debitAccount.getCurrencyCode();
        int creditCurrencyCode = creditAccount.getCurrencyCode();

        if (!Objects.equals(debitCurrencyCode, creditCurrencyCode)) {
            throw new CurrencyCodeMismatchException("debit account currency code " + debitCurrencyCode +
                    " differs from credit account " + creditCurrencyCode);
        }

        log.info("Creating new transaction, debitAccountId=[{}], creditAccountId=[{}], description=[{}], amount=[{}]",
                debitAccount.getId(), creditAccount.getId(), description, amount);

        accountService.withdraw(debitAccount, amount);
        accountService.deposit(creditAccount, amount);

        Transaction transaction = new Transaction(
                debitAccount,
                creditAccount,
                TransactionStatus.PENDING,
                amount,
                description
        );

        transactionRepository.save(transaction);

        return transaction;
    }
}
