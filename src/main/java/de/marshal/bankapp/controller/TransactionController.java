package de.marshal.bankapp.controller;

import de.marshal.bankapp.dto.transaction.CreateTransactionDTO;
import de.marshal.bankapp.dto.transaction.TransactionDTO;
import de.marshal.bankapp.entity.Transaction;
import de.marshal.bankapp.exception.AccountNotFoundException;
import de.marshal.bankapp.exception.CurrencyCodeMismatchException;
import de.marshal.bankapp.exception.InsufficientFundsException;
import de.marshal.bankapp.mapper.TransactionMapper;
import de.marshal.bankapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> search(@RequestParam long accountId) {
        List<Transaction> transactions = transactionService.findByAccountId(accountId);

        return ResponseEntity.ok(transactionMapper.transactionListToTransactionDTOList(transactions));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<TransactionDTO> create(@RequestBody CreateTransactionDTO createTransactionDTO) {
        try {
            Transaction transaction = transactionService.create(
                    createTransactionDTO.getDebitAccountId(),
                    createTransactionDTO.getCreditAccountId(),
                    createTransactionDTO.getAmount(),
                    createTransactionDTO.getDescription()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(transactionMapper.transactionToTransactionDTO(transaction));
        } catch (InsufficientFundsException | AccountNotFoundException | CurrencyCodeMismatchException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, null, ex);
        }
    }
}
