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
        Account debitAccount = accountRepository.findById(debitAccountId)
                .orElseThrow(() -> new AccountNotFoundException("account with id " + debitAccountId + " not found"));

        Long debitAccountBalance = debitAccount.getBalance();
        if (debitAccountBalance < amount) {
            throw new InsufficientFundsException("unable to withdraw " + amount + " from " + debitAccountBalance + " available");
        }

        Account creditAccount = accountRepository.findById(creditAccountId)
                .orElseThrow(() -> new AccountNotFoundException("account with id " + creditAccountId + " not found"));

        Long creditAccountBalance = creditAccount.getBalance();

        Integer debitCurrencyCode = debitAccount.getCurrencyCode();
        Integer creditCurrencyCode = creditAccount.getCurrencyCode();
        if (!Objects.equals(debitCurrencyCode, creditCurrencyCode)) {
            throw new CurrencyCodeMismatchException("debit account currency code " + debitCurrencyCode +
                    " differs from credit account " + creditCurrencyCode);
        }

        Transaction transaction = new Transaction(
                debitAccount,
                creditAccount,
                TransactionStatus.PENDING,
                amount,
                description
        );

        debitAccount.setBalance(debitAccountBalance - amount);
        creditAccount.setBalance(creditAccountBalance + amount);

        transactionRepository.save(transaction);
        accountRepository.save(debitAccount);
        accountRepository.save(creditAccount);

        return transaction;
    }
}
