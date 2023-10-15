package de.marshal.bankapp.controller;

import de.marshal.bankapp.dto.account.AccountDTO;
import de.marshal.bankapp.dto.account.CreateAccountDTO;
import de.marshal.bankapp.entity.Account;
import de.marshal.bankapp.exception.ClientNotFoundException;
import de.marshal.bankapp.mapper.AccountMapper;
import de.marshal.bankapp.service.AccountService;
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
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    public ResponseEntity<List<AccountDTO>> search(@RequestParam long clientId) {
        List<Account> accounts = accountService.findByClientId(clientId);

        return ResponseEntity.ok(accountMapper.accountListToAccountDTOList(accounts));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<AccountDTO> create(@RequestBody CreateAccountDTO createAccountDTO) {
        try {
            Account account = accountService.create(
                    createAccountDTO.getClientId(),
                    createAccountDTO.getName(),
                    createAccountDTO.getCurrencyCode()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(accountMapper.accountToAccountDTO(account));
        } catch (ClientNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, null, ex);
        }
    }
}
