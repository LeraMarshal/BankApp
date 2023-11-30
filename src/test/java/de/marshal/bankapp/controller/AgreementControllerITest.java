package de.marshal.bankapp.controller;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.dto.agreement.AgreementDTO;
import de.marshal.bankapp.dto.agreement.CreateAgreementDTO;
import de.marshal.bankapp.entity.Account;
import de.marshal.bankapp.entity.Agreement;
import de.marshal.bankapp.entity.AgreementStatus;
import de.marshal.bankapp.exception.ApplicationException;
import de.marshal.bankapp.exception.ApplicationExceptionCode;
import de.marshal.bankapp.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext
@AutoConfigureMockMvc
public class AgreementControllerITest extends AppITests {
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void searchByClientIdReturnsCorrectAccountsTest() throws Exception {
        // When
        MvcResult mvcResult = doGet("/agreement?clientId=1");
        List<AgreementDTO> response = unmarshalListJson(mvcResult, AgreementDTO.class);

        // Then
        assertMvcStatus(HttpStatus.OK, mvcResult);
        assertEquals(1, response.size());
    }

    @Test
    public void searchWithNullClientIdExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doGet("/agreement?clientId=");

        // Then
        assertMvcError(ApplicationExceptionCode.UNSPECIFIED, mvcResult);
    }

    @Test
    public void getByIdTest() throws Exception {
        // When
        MvcResult mvcResult = doGet("/agreement/1");
        AgreementDTO agreementDTO = unmarshalJson(mvcResult, AgreementDTO.class);

        // Then
        assertMvcStatus(HttpStatus.OK, mvcResult);
        assertEquals(1L, agreementDTO.getId());
        assertEquals(AgreementStatus.ACTIVE, agreementDTO.getStatus());
        assertEquals(-5, agreementDTO.getInterestRate());
        assertEquals(0L, agreementDTO.getDebt());
    }

    @Test
    public void getByIdExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doGet("/agreement/0");

        // Then
        assertMvcError(ApplicationExceptionCode.AGREEMENT_NOT_FOUND, mvcResult);
    }

    @Test
    public void deleteByIdTest() throws Exception {
        // When
        MvcResult mvcResult = doDelete("/agreement/4");
        AgreementDTO agreementDTO = unmarshalJson(mvcResult, AgreementDTO.class);

        // Then
        assertMvcStatus(HttpStatus.OK, mvcResult);
        assertEquals(4, agreementDTO.getId());
        assertEquals(AgreementStatus.INACTIVE, agreementDTO.getStatus());
    }

    @Test
    public void deleteByIdInvalidStatusExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doDelete("/agreement/5");

        // Then
        assertMvcError(ApplicationExceptionCode.INVALID_STATUS, mvcResult);
    }

    @Test
    public void deleteByIdUnfulfilledObligationsExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doDelete("/agreement/6");

        // Then
        assertMvcError(ApplicationExceptionCode.UNFULFILLED_OBLIGATIONS, mvcResult);
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(longs = {7, 8})
    public void confirmTest(long id) throws Exception {
        // Given
        Account account = accountRepository.findById(2L).orElseThrow();
        long initialBalance = account.getBalance();

        // When
        MvcResult mvcResult = doPost("/agreement/" + id +"/confirm", null);
        AgreementDTO agreementDTO = unmarshalJson(mvcResult, AgreementDTO.class);
        Account account1 = accountRepository.findById(2L).orElseThrow();

        // Then
        assertEquals(AgreementStatus.ACTIVE, agreementDTO.getStatus());
        assertEquals(initialBalance + agreementDTO.getDebt(), account1.getBalance());
    }

    @Test
    public void confirmInvalidStatusExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doPost("/agreement/6/confirm", null);

        // Then
        assertMvcError(ApplicationExceptionCode.INVALID_STATUS, mvcResult);
    }

    @Test
    public void confirmInsufficientFundsExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doPost("/agreement/9/confirm", null);

        // Then
        assertMvcError(ApplicationExceptionCode.INSUFFICIENT_FUNDS, mvcResult);
    }

    @Test
    public void createTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/agreement", new CreateAgreementDTO(
                2L,
                1L,
                0,
                100000L
        ));
        AgreementDTO agreementDTO = unmarshalJson(mvcResult, AgreementDTO.class);

        // Then
        assertMvcStatus(HttpStatus.CREATED, mvcResult);
        assertEquals(AgreementStatus.PENDING, agreementDTO.getStatus());
        assertEquals(-100000L, agreementDTO.getDebt());
    }

    @Test
    public void createInvalidAccountStatusExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/agreement", new CreateAgreementDTO(
                4L,
                1L,
                0,
                100000L
        ));

        // Then
        assertMvcError(ApplicationExceptionCode.INVALID_STATUS, mvcResult);
    }

    @Test
    public void createInvalidProductStatusExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/agreement", new CreateAgreementDTO(
                2L,
                3L,
                0,
                100000L
        ));

        // Then
        assertMvcError(ApplicationExceptionCode.INVALID_STATUS, mvcResult);
    }

    @Test
    public void createInvalidInterestRateExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/agreement", new CreateAgreementDTO(
                2L,
                1L,
                -10,
                100000L
        ));

        // Then
        assertMvcError(ApplicationExceptionCode.INVALID_INTEREST_RATE, mvcResult);
    }

    @Test
    public void createInvalidAmountExceptionTest() throws Exception {
        // When
        MvcResult mvcResult = doPut("/agreement", new CreateAgreementDTO(
                2L,
                1L,
                -5,
                50000000L
        ));

        // Then
        assertMvcError(ApplicationExceptionCode.INVALID_AMOUNT, mvcResult);
    }

    @Test
    public void createCurrencyCodeMismatchException() throws Exception {
        // When
        MvcResult mvcResult = doPut("/agreement", new CreateAgreementDTO(
                2L,
                4L,
                5,
                50000L
        ));

        // Then
        assertMvcError(ApplicationExceptionCode.CURRENCY_CODE_MISMATCH, mvcResult);
    }
}
