package de.marshal.bankapp.controller;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.dto.account.AccountDTO;
import de.marshal.bankapp.dto.account.CreateAccountDTO;
import de.marshal.bankapp.entity.AccountStatus;
import de.marshal.bankapp.exception.ApplicationException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@AutoConfigureMockMvc
public class AccountControllerITest extends AppITests {
    @Test
    public void searchByClientIdReturnsCorrectAccountsTest() throws Exception {
        MvcResult mvcResult = doGet("/account?clientId=1");

        assertStatus(HttpStatus.OK, mvcResult);

        List<AccountDTO> accounts = unmarshalListJson(mvcResult, AccountDTO.class);
        assertEquals(1, accounts.size());
    }

    @Test
    public void searchWithNullClientIdExceptionTest() throws Exception {
        MvcResult mvcResult = doGet("/account?clientId=");

        assertExceptionDTO(ApplicationException.UNSPECIFIED, mvcResult);
    }

    @Test
    public void createReturnsCorrectAccountTest() throws Exception {
        MvcResult mvcResult = doPut("/account", new CreateAccountDTO(1L, "test", 978));

        assertStatus(HttpStatus.CREATED, mvcResult);

        AccountDTO account = unmarshalJson(mvcResult, AccountDTO.class);
        assertEquals(3L, account.getId());
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
    }

    @Test
    public void createNoClientExceptionTest() throws Exception {
        MvcResult mvcResult = doPut("/account", new CreateAccountDTO(0L, "test", 978));

        assertExceptionDTO(ApplicationException.CLIENT_NOT_FOUND, mvcResult);
    }
}
