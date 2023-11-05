package de.marshal.bankapp.controller;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.configuration.BankConfigurationProperties;
import de.marshal.bankapp.dto.account.AccountDTO;
import de.marshal.bankapp.dto.account.CreateAccountDTO;
import de.marshal.bankapp.dto.client.ClientDTO;
import de.marshal.bankapp.entity.*;
import de.marshal.bankapp.exception.ApplicationExceptionCode;
import de.marshal.bankapp.repository.AccountRepository;
import de.marshal.bankapp.repository.ClientRepository;
import de.marshal.bankapp.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@AutoConfigureMockMvc
public class AccountControllerITest extends AppITests {
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void searchTest() throws Exception {
        // When
        MvcResult mvcResult = doGet("/account?clientId=1");
        List<AccountDTO> accounts = unmarshalListJson(mvcResult, AccountDTO.class);

        // Then
        assertMvcStatus(HttpStatus.OK, mvcResult);
        assertEquals(1, accounts.size());
    }

    @Test
    public void searchWithNullClientIdExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doGet("/account?clientId=");

        // Then
        assertMvcError(ApplicationExceptionCode.UNSPECIFIED, mvcResult);
    }

    @Test
    public void getByIdTest() throws Exception {
        // When
        MvcResult mvcResult = doGet("/account/1");
        AccountDTO accountDTO = unmarshalJson(mvcResult, AccountDTO.class);

        // Then
        assertMvcStatus(HttpStatus.OK, mvcResult);

        assertEquals(new AccountDTO(
                1L,
                "debit",
                AccountStatus.ACTIVE,
                150000L,
                978
        ), accountDTO);
    }

    @Test
    public void getByIdExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doGet("/account/0");

        // Then
        assertMvcError(ApplicationExceptionCode.ACCOUNT_NOT_FOUND, mvcResult);
    }

    @Test
    @Transactional
    public void createTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/account", new CreateAccountDTO(2L, "test", 978));
        AccountDTO account = unmarshalJson(mvcResult, AccountDTO.class);

        // Then
        assertMvcStatus(HttpStatus.CREATED, mvcResult);

        assertNotNull(accountRepository.findById(account.getId()).orElseThrow());

        assertEquals(AccountStatus.ACTIVE, account.getStatus());
        assertEquals("test", account.getName());
        assertEquals(978, account.getCurrencyCode());
    }

    @Test
    public void createNoClientExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/account", new CreateAccountDTO(0L, "test", 978));

        // Then
        assertMvcError(ApplicationExceptionCode.CLIENT_NOT_FOUND, mvcResult);
    }

    @Test
    public void deleteTest() throws Exception {
        // When
        MvcResult mvcResult = doDelete("/account/3");
        AccountDTO accountDTO = unmarshalJson(mvcResult, AccountDTO.class);

        // Then
        assertMvcStatus(HttpStatus.OK, mvcResult);
        assertEquals(AccountStatus.DELETED, accountDTO.getStatus());
    }

    @Test
    public void deleteStatusExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doDelete("/account/4");

        // Then
        assertMvcError(ApplicationExceptionCode.INVALID_STATUS, mvcResult);
    }

    @Test
    public void deleteBalanceExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doDelete("/account/5");

        // Then
        assertMvcError(ApplicationExceptionCode.UNFULFILLED_OBLIGATIONS, mvcResult);
    }

    @Test
    public void deleteAgreementsExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doDelete("/account/6");

        // Then
        assertMvcError(ApplicationExceptionCode.UNFULFILLED_OBLIGATIONS, mvcResult);
    }
}
