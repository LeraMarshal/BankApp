package de.marshal.bankapp.controller;

import de.marshal.bankapp.AppITests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@DirtiesContext
@AutoConfigureMockMvc
public class ClientControllerITest extends AppITests {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void searchByPhoneEmailReturnsBadRequestTest() throws Exception{
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/client/search?phone=abc&email=abc")
        ).andReturn();

        Assertions.assertEquals(400, mvcResult.getResponse().getStatus());
    }
}
