package de.marshal.bankapp.controller;

import de.marshal.bankapp.dto.ResponseDTO;
import de.marshal.bankapp.dto.account.AccountDTO;
import de.marshal.bankapp.dto.account.CreateAccountDTO;
import de.marshal.bankapp.entity.Account;
import de.marshal.bankapp.exception.ApplicationException;
import de.marshal.bankapp.mapper.AccountMapper;
import de.marshal.bankapp.service.AccountService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<List<AccountDTO>> search(
            @RequestParam long clientId
    ) {
        List<Account> accounts = accountService.findByClientId(clientId);

        return ResponseDTO.ok(accountMapper.accountListToAccountDTOList(accounts));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<AccountDTO> get(
            @PathVariable long id
    ) throws ApplicationException {
        Account account = accountService.getById(id);

        return ResponseDTO.ok(accountMapper.accountToAccountDTO(account));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO<AccountDTO> create(
            @RequestBody CreateAccountDTO createAccountDTO
    ) throws ApplicationException {
        Account account = accountService.create(
                createAccountDTO.getClientId(),
                createAccountDTO.getName(),
                createAccountDTO.getCurrencyCode()
        );

        return ResponseDTO.ok(accountMapper.accountToAccountDTO(account));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO<AccountDTO> delete(
            @PathVariable long id
    ) throws ApplicationException {
        Account account = accountService.deleteById(id);

        return ResponseDTO.ok(accountMapper.accountToAccountDTO(account));
    }
}
