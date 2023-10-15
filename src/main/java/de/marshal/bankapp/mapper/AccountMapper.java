package de.marshal.bankapp.mapper;

import de.marshal.bankapp.dto.account.AccountDTO;
import de.marshal.bankapp.dto.account.CreateAccountDTO;
import de.marshal.bankapp.entity.Account;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO accountToAccountDTO(Account account);

    List<AccountDTO> accountListToAccountDTOList(List<Account> accounts);

    Account createAccountDTOToAccount(CreateAccountDTO createAccountDTO);
}
