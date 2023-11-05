package de.marshal.bankapp.controller;

import de.marshal.bankapp.dto.ResponseDTO;
import de.marshal.bankapp.dto.transaction.CreateTransactionDTO;
import de.marshal.bankapp.dto.transaction.TransactionDTO;
import de.marshal.bankapp.entity.Transaction;
import de.marshal.bankapp.exception.ApplicationException;
import de.marshal.bankapp.mapper.TransactionMapper;
import de.marshal.bankapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<List<TransactionDTO>> search(
            @RequestParam long accountId
    ) {
        List<Transaction> transactions = transactionService.findByAccountId(accountId);

        return ResponseDTO.ok(transactionMapper.transactionListToTransactionDTOList(transactions));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO<TransactionDTO> create(
            @RequestBody CreateTransactionDTO createTransactionDTO
    ) throws ApplicationException {
        Transaction transaction = transactionService.create(
                createTransactionDTO.getDebitAccountId(),
                createTransactionDTO.getCreditAccountId(),
                createTransactionDTO.getDescription(),
                createTransactionDTO.getAmount()
        );

        return ResponseDTO.ok(transactionMapper.transactionToTransactionDTO(transaction));
    }
}
