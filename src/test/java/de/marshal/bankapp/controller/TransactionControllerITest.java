package de.marshal.bankapp.controller;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.dto.transaction.CreateTransactionDTO;
import de.marshal.bankapp.dto.transaction.TransactionDTO;
import de.marshal.bankapp.exception.ApplicationException;
import de.marshal.bankapp.exception.ApplicationExceptionCode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext
@AutoConfigureMockMvc
public class TransactionControllerITest extends AppITests {
    @Test
    public void searchByClientIdReturnsCorrectAccountsTest() throws Exception {
        MvcResult mvcResult = doGet("/transaction?accountId=1");

        assertMvcStatus(HttpStatus.OK, mvcResult);

        List<TransactionDTO> transactions = unmarshalListJson(mvcResult, TransactionDTO.class);
        assertEquals(2, transactions.size());
    }

    @Test
    public void createReturnsCorrectTransactionTest() throws Exception {
        Timestamp timestamp = Timestamp.from(Instant.now());

        MvcResult mvcResult = doPut("/transaction", new CreateTransactionDTO(1L, 2L, 10000L, "test"));

        assertMvcStatus(HttpStatus.CREATED, mvcResult);

        TransactionDTO transaction = unmarshalJson(mvcResult, TransactionDTO.class);
        assertEquals(3L, transaction.getId());
        assertEquals(1L, transaction.getDebitAccountId());
        assertEquals(2L, transaction.getCreditAccountId());
        assertEquals(10000L, transaction.getAmount());
        assertEquals("test", transaction.getDescription());
        assertTrue(timestamp.before(transaction.getCreatedAt()));
    }

    @Test
    public void createInsufficientFundsExceptionTest() throws Exception {
        MvcResult mvcResult = doPut("/transaction", new CreateTransactionDTO(1L, 2L, 1000000L, "test"));

        assertMvcError(ApplicationExceptionCode.INSUFFICIENT_FUNDS, mvcResult);
    }

    @Test
    public void createNonExistingAccountExceptionTest() throws Exception {
        MvcResult mvcResult = doPut("/transaction", new CreateTransactionDTO(3L, 2L, 10000L, "test"));

        assertMvcError(ApplicationExceptionCode.ACCOUNT_NOT_FOUND, mvcResult);
    }
}
