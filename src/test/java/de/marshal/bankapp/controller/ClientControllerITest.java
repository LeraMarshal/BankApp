package de.marshal.bankapp.controller;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.dto.account.AccountDTO;
import de.marshal.bankapp.dto.client.ClientDTO;
import de.marshal.bankapp.dto.client.ClientWithAccountsDTO;
import de.marshal.bankapp.dto.client.RegisterClientDTO;
import de.marshal.bankapp.entity.Account;
import de.marshal.bankapp.entity.AccountStatus;
import de.marshal.bankapp.entity.Client;
import de.marshal.bankapp.entity.ClientStatus;
import de.marshal.bankapp.exception.ApplicationException;
import de.marshal.bankapp.exception.ApplicationExceptionCode;
import de.marshal.bankapp.repository.AccountRepository;
import de.marshal.bankapp.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext
public class ClientControllerITest extends AppITests {
    @Autowired
    ClientRepository clientRepository;

    @Test
    public void searchByPhoneTest() throws Exception {
        verifyClient(doGet("/client?phone=493048898888"));
    }

    @Test
    public void searchByEmailTest() throws Exception {
        verifyClient(doGet("/client?email=john.smith@gmail.com"));
    }

    @Test
    public void searchByPhoneEmailExceptionTest() throws Exception {
        MvcResult mvcResult = doGet("/client?phone=abc&email=abc");

        assertMvcError(ApplicationExceptionCode.ILLEGAL_SEARCH_PARAMS, mvcResult);
    }

    @Test
    public void searchByNonExistingPhoneExceptionTest() throws Exception {
        MvcResult mvcResult = doGet("/client?phone=490000000000");

        assertMvcError(ApplicationExceptionCode.CLIENT_NOT_FOUND, mvcResult);
    }

    @Test
    public void searchByNonExistingEmailExceptionTest() throws Exception {
        MvcResult mvcResult = doGet("/client?email=not.existing@gmail.com");

        assertMvcError(ApplicationExceptionCode.CLIENT_NOT_FOUND, mvcResult);
    }

    @Test
    public void getByIdTest() throws Exception {
        MvcResult mvcResult = doGet("/client/1");
        assertMvcStatus(HttpStatus.OK, mvcResult);

        verifyClientWithAccounts(mvcResult, new ClientWithAccountsDTO(
                1L,
                ClientStatus.ACTIVE,
                "John",
                "Smith",
                "john.smith@gmail.com",
                "Berlin, Harden str. 4",
                "493048898888",
                List.of(new AccountDTO(
                        1L,
                        "debit",
                        AccountStatus.ACTIVE,
                        150000,
                        978
                ))
        ));
    }

    @Test
    public void getByNonExistingIdExceptionTest() throws Exception {
        MvcResult mvcResult = doGet("/client/0");

        assertMvcError(ApplicationExceptionCode.CLIENT_NOT_FOUND, mvcResult);
    }

    @Test
    @Transactional
    public void registerEurTest() throws Exception {
        MvcResult mvcResult = doPut("/client", new RegisterClientDTO(
                "Vasilii",
                "Rio",
                "vasiario@gmail.com",
                "Germany, Berlin",
                "49100100100",
                978
        ));

        assertMvcStatus(HttpStatus.CREATED, mvcResult);

        Client client = clientRepository.findByEmail("vasiario@gmail.com").orElseThrow();
        verifyClientWithAccounts(mvcResult, new ClientWithAccountsDTO(
                client.getId(),
                ClientStatus.ACTIVE,
                "Vasilii",
                "Rio",
                "vasiario@gmail.com",
                "Germany, Berlin",
                "49100100100",
                List.of(new AccountDTO(
                        client.getAccounts().get(0).getId(),
                        "DEFAULT PRODUCT EUR",
                        AccountStatus.ACTIVE,
                        0,
                        978
                ))
        ));
    }

    @Test
    @Transactional
    public void registerUsdTest() throws Exception {
        MvcResult mvcResult = doPut("/client", new RegisterClientDTO(
                "Vasilii",
                "Rio",
                "vasiario1@gmail.com",
                "Germany, Berlin",
                "49100100101",
                840
        ));

        assertMvcStatus(HttpStatus.CREATED, mvcResult);

        Client client = clientRepository.findByEmail("vasiario1@gmail.com").orElseThrow();
        verifyClientWithAccounts(mvcResult, new ClientWithAccountsDTO(
                client.getId(),
                ClientStatus.ACTIVE,
                "Vasilii",
                "Rio",
                "vasiario1@gmail.com",
                "Germany, Berlin",
                "49100100101",
                List.of(new AccountDTO(
                        client.getAccounts().get(0).getId(),
                        "DEFAULT PRODUCT USD",
                        AccountStatus.ACTIVE,
                        0,
                        840
                ))
        ));
    }

    @Test
    public void registerDuplicateEmailExceptionTest() throws Exception {
        MvcResult mvcResult = doPut("/client", new RegisterClientDTO(
                "John",
                "Smith",
                "john.smith@gmail.com",
                "Germany, Berlin",
                "49100100100",
                978
        ));

        assertMvcError(ApplicationExceptionCode.UNSPECIFIED, mvcResult);
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

    private void verifyClientWithAccounts(MvcResult mvcResult, ClientWithAccountsDTO clientWithAccountsDTO) throws Exception {
        assertEquals(clientWithAccountsDTO, unmarshalJson(mvcResult, ClientWithAccountsDTO.class));
    }
}
