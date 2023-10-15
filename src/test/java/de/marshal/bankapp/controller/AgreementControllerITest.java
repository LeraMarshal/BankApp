package de.marshal.bankapp.controller;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.dto.ExceptionDTO;
import de.marshal.bankapp.dto.agreement.AgreementDTO;
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

        assertStatus(HttpStatus.OK, mvcResult);

        List<AgreementDTO> response = unmarshalListJson(mvcResult, AgreementDTO.class);
        assertEquals(1, response.size());
    }

    @Test
    public void searchWithNullClientIdReturnsBadRequestTest() throws Exception {
        MvcResult mvcResult = doGet("/agreement?clientId=");

        assertExceptionDTO(HttpStatus.INTERNAL_SERVER_ERROR, mvcResult);
    }
}
