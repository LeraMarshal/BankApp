package de.marshal.bankapp.controller;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.dto.agreement.AgreementDTO;
import de.marshal.bankapp.exception.ApplicationException;
import de.marshal.bankapp.exception.ApplicationExceptionCode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext
@AutoConfigureMockMvc
public class AgreementControllerITest extends AppITests {
    @Test
    public void searchByClientIdReturnsCorrectAccountsTest() throws Exception {
        MvcResult mvcResult = doGet("/agreement?clientId=1");

        assertMvcStatus(HttpStatus.OK, mvcResult);

        List<AgreementDTO> response = unmarshalListJson(mvcResult, AgreementDTO.class);
        assertEquals(1, response.size());
    }

    @Test
    public void searchWithNullClientIdExceptionTest() throws Exception {
        MvcResult mvcResult = doGet("/agreement?clientId=");

        assertMvcError(ApplicationExceptionCode.UNSPECIFIED, mvcResult);
    }
}
