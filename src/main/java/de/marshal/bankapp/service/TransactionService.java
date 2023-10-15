package de.marshal.bankapp.service;

import de.marshal.bankapp.entity.Account;
import de.marshal.bankapp.entity.Transaction;
import de.marshal.bankapp.entity.TransactionStatus;
import de.marshal.bankapp.exception.AccountNotFoundException;
import de.marshal.bankapp.exception.CurrencyCodeMismatchException;
import de.marshal.bankapp.exception.InsufficientFundsException;
import de.marshal.bankapp.repository.AccountRepository;
import de.marshal.bankapp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public List<Transaction> findByAccountId(long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public Transaction create(
            long debitAccountId,
            long creditAccountId,
            long amount,
            String description
    ) throws AccountNotFoundException, InsufficientFundsException, CurrencyCodeMismatchException {
        Account debitAccount = accountRepository.findById(debitAccountId).orElseThrow(AccountNotFoundException::new);

        if (debitAccount.getBalance() < amount) {
            throw new InsufficientFundsException();
        }

        Account creditAccount = accountRepository.findById(creditAccountId).orElseThrow(AccountNotFoundException::new);

        if (!Objects.equals(debitAccount.getCurrencyCode(), creditAccount.getCurrencyCode())) {
            throw new CurrencyCodeMismatchException();
        }

        Transaction transaction = new Transaction(
                debitAccount,
                creditAccount,
                TransactionStatus.PENDING,
                amount,
                description
        );

        debitAccount.setBalance(debitAccount.getBalance() - amount);
        creditAccount.setBalance(creditAccount.getBalance() + amount);

        transactionRepository.save(transaction);
        accountRepository.save(debitAccount);
        accountRepository.save(creditAccount);

        return transaction;
    }
}
