package de.marshal.bankapp.mapper;

import de.marshal.bankapp.dto.transaction.TransactionDTO;
import de.marshal.bankapp.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "debitAccountId", source = "transaction.debitAccount.id")
    @Mapping(target = "creditAccountId", source = "transaction.creditAccount.id")
    TransactionDTO transactionToTransactionDTO(Transaction transaction);
    List<TransactionDTO> transactionListToTransactionDTOList(List<Transaction> transactions);
}
