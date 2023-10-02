package de.marshal.bankapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.dto.AccountDTO;
import de.marshal.bankapp.dto.ClientDTO;
import de.marshal.bankapp.dto.ClientWithAccountsDTO;
import de.marshal.bankapp.entity.AccountStatus;
import de.marshal.bankapp.entity.ClientStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

@DirtiesContext
@AutoConfigureMockMvc
public class ClientControllerITest extends AppITests {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

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

        Assertions.assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    public void searchByNonExistingPhoneReturnsNotFoundTest() throws Exception {
        MvcResult mvcResult = doGet("/client?phone=490000000000");

        Assertions.assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void searchByNonExistingEmailReturnsNotFoundTest() throws Exception {
        MvcResult mvcResult = doGet("/client?email=not.existing@gmail.com");

        Assertions.assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void getByIdReturnsCorrectClientTest() throws Exception {
        verifyClientWithAccounts(doGet("/client/1"));
    }

    @Test
    public void getByNonExistingIdReturnsNotFoundTest() throws Exception {
        MvcResult mvcResult = doGet("/client/0");

        Assertions.assertEquals(404, mvcResult.getResponse().getStatus());
    }

    private MvcResult doGet(String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
    }

    private void verifyClient(MvcResult mvcResult) throws Exception {
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());

        String response = mvcResult.getResponse().getContentAsString();

        ClientDTO clientDTO = objectMapper.readValue(response, ClientDTO.class);

        Assertions.assertEquals(new ClientDTO(
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
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());

        String response = mvcResult.getResponse().getContentAsString();

        ClientWithAccountsDTO clientWithAccountsDTO = objectMapper.readValue(response, ClientWithAccountsDTO.class);

        Assertions.assertEquals(new ClientWithAccountsDTO(
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
