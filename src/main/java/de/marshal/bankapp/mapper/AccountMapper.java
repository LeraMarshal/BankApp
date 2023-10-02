package de.marshal.bankapp.mapper;

import de.marshal.bankapp.dto.AccountDTO;
import de.marshal.bankapp.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO accountToAccountDTO(Account account);
}
