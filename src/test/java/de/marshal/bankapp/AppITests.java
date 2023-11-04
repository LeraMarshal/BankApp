package de.marshal.bankapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.marshal.bankapp.dto.ExceptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// https://spring.io/blog/2023/06/23/improved-testcontainers-support-in-spring-boot-3-1
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
public class AppITests {
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Container // запускает контейнер
    @ServiceConnection // выставляет свойства datasource
    // https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/
    protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    protected MockMvc mockMvc;

    protected MvcResult doGet(String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
    }

    protected MvcResult doPut(String url, Object content) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsBytes(content))
        ).andReturn();
    }

    protected void assertStatus(HttpStatus status, MvcResult mvcResult) {
        assertEquals(status.value(), mvcResult.getResponse().getStatus());
    }

    protected void assertExceptionDTO(int code, MvcResult mvcResult) throws Exception {
        HttpStatus expectedStatus;

        if (code == 0) {
            expectedStatus = HttpStatus.OK;
        } else if (code < 0) {
            expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            expectedStatus = HttpStatus.BAD_REQUEST;
        }

        assertStatus(expectedStatus, mvcResult);

        ExceptionDTO exceptionDTO = unmarshalJson(mvcResult, ExceptionDTO.class);
        assertEquals(code, exceptionDTO.getCode());
    }

    protected <T> List<T> unmarshalListJson(MvcResult from, Class<T> clazz) throws Exception {
        return OBJECT_MAPPER.readValue(
                from.getResponse().getContentAsString(),
                OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz)
        );
    }

    protected <T> T unmarshalJson(MvcResult from, Class<T> clazz) throws Exception {
        return OBJECT_MAPPER.readValue(
                from.getResponse().getContentAsString(),
                clazz
        );
    }
}
