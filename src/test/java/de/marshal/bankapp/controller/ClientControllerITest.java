package de.marshal.bankapp.controller;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.dto.account.AccountDTO;
import de.marshal.bankapp.dto.client.ClientDTO;
import de.marshal.bankapp.dto.client.ClientWithAccountsDTO;
import de.marshal.bankapp.dto.client.RegisterClientDTO;
import de.marshal.bankapp.entity.AccountStatus;
import de.marshal.bankapp.entity.ClientStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext
public class ClientControllerITest extends AppITests {
    @Test
    public void searchByPhoneReturnsCorrectClientTest() throws Exception {
        verifyClient(doGet("/client?phone=493048898888"));
    }

    @Test
    public void searchByEmailReturnsCorrectClientTest() throws Exception {
        verifyClient(doGet("/client?email=john.smith@gmail.com"));
    }

    @Test
    public void searchByPhoneEmailReturnsBadRequestTest() throws Exception {
        MvcResult mvcResult = doGet("/client?phone=abc&email=abc");

        assertExceptionDTO(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void searchByNonExistingPhoneReturnsNotFoundTest() throws Exception {
        MvcResult mvcResult = doGet("/client?phone=490000000000");

        assertStatus(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void searchByNonExistingEmailReturnsNotFoundTest() throws Exception {
        MvcResult mvcResult = doGet("/client?email=not.existing@gmail.com");

        assertStatus(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void getByIdReturnsCorrectClientTest() throws Exception {
        verifyClientWithAccounts(doGet("/client/1"));
    }

    @Test
    public void getByNonExistingIdReturnsNotFoundTest() throws Exception {
        MvcResult mvcResult = doGet("/client/0");

        assertStatus(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void registerReturnsCorrectClientTest() throws Exception {
        MvcResult mvcResult = doPut("/client", new RegisterClientDTO(
                "Vasilii",
                "Rio",
                "vasiario@gmail.com",
                "Germany, Berlin",
                "49100100100"
        ));

        assertStatus(HttpStatus.CREATED, mvcResult);

        ClientWithAccountsDTO client = unmarshalJson(mvcResult, ClientWithAccountsDTO.class);

        assertEquals(3, client.getId());
        assertEquals(1, client.getAccounts().size());
        assertEquals(3, client.getAccounts().get(0).getId());
    }

    private void verifyClient(MvcResult mvcResult) throws Exception {
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        ClientDTO clientDTO = unmarshalJson(mvcResult, ClientDTO.class);

        assertEquals(new ClientDTO(
                1L,
                ClientStatus.ACTIVE,
                "John",
                "Smith",
                "john.smith@gmail.com",
                "Berlin, Harden str. 4",
                "493048898888"
        ), clientDTO);
    }

    private void verifyClientWithAccounts(MvcResult mvcResult) throws Exception {
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        ClientWithAccountsDTO clientWithAccountsDTO = unmarshalJson(mvcResult, ClientWithAccountsDTO.class);

        assertEquals(new ClientWithAccountsDTO(
                1L,
                ClientStatus.ACTIVE,
                "John",
                "Smith",
                "john.smith@gmail.com",
                "Berlin, Harden str. 4",
                "493048898888",
                List.of(
                        new AccountDTO(
                                1L,
                                "debit",
                                AccountStatus.ACTIVE,
                                150000L,
                                978
                        )
                )
        ), clientWithAccountsDTO);
    }
}
