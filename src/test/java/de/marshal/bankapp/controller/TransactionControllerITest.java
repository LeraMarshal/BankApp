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
    public void searchByClientIdTest() throws Exception {
        // When
        MvcResult mvcResult = doGet("/transaction?accountId=1");
        List<TransactionDTO> transactions = unmarshalListJson(mvcResult, TransactionDTO.class);

        // Then
        assertMvcStatus(HttpStatus.OK, mvcResult);

        assertEquals(2, transactions.size());
    }

    @Test
    public void createTest() throws Exception {
        // Given
        Timestamp timestamp = Timestamp.from(Instant.now());

        // When
        MvcResult mvcResult = doPut("/transaction", new CreateTransactionDTO(1L, 2L, 10000L, "test"));
        TransactionDTO transaction = unmarshalJson(mvcResult, TransactionDTO.class);

        // Then
        assertMvcStatus(HttpStatus.CREATED, mvcResult);

        assertEquals(3L, transaction.getId());
        assertEquals(1L, transaction.getDebitAccountId());
        assertEquals(2L, transaction.getCreditAccountId());
        assertEquals(10000L, transaction.getAmount());
        assertEquals("test", transaction.getDescription());

        assertTrue(timestamp.before(transaction.getCreatedAt()));
    }

    @Test
    public void createInsufficientFundsExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/transaction", new CreateTransactionDTO(
                1L,
                2L,
                1000000L,
                "test"
        ));

        // Then
        assertMvcError(ApplicationExceptionCode.INSUFFICIENT_FUNDS, mvcResult);
    }

    @Test
    public void createNonExistingAccountExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/transaction", new CreateTransactionDTO(
                0L,
                0L,
                10000L,
                "test"
        ));

        // Then
        assertMvcError(ApplicationExceptionCode.ACCOUNT_NOT_FOUND, mvcResult);
    }

    @Test
    public void createInvalidDebitAccountStatusExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/transaction", new CreateTransactionDTO(
                4L,
                1L,
                10000L,
                "test"
        ));

        // Then
        assertMvcError(ApplicationExceptionCode.INVALID_STATUS, mvcResult);
    }

    @Test
    public void createInvalidCreditAccountStatusExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/transaction", new CreateTransactionDTO(
                1L,
                4L,
                10000L,
                "test"
        ));

        // Then
        assertMvcError(ApplicationExceptionCode.INVALID_STATUS, mvcResult);
    }

    @Test
    public void createCurrencyCodeMismatchExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/transaction", new CreateTransactionDTO(
                7L,
                1L,
                10000L,
                "test"
        ));

        // Then
        assertMvcError(ApplicationExceptionCode.CURRENCY_CODE_MISMATCH, mvcResult);
    }
}
