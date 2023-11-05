package de.marshal.bankapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.marshal.bankapp.dto.ResponseDTO;
import de.marshal.bankapp.exception.ApplicationExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
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

    @Value("${jwt.test.auth-token}")
    protected String authToken;

    @Container // запускает контейнер
    @ServiceConnection // выставляет свойства datasource
    // https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/
    protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Autowired
    protected MockMvc mockMvc;

    protected MvcResult doGet(String url) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
        ).andReturn();
    }

    protected MvcResult doDelete(String url) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .delete(url)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
        ).andReturn();
    }

    protected MvcResult doPost(String url, Object content) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .content(OBJECT_MAPPER.writeValueAsBytes(content))
        ).andReturn();
    }

    protected MvcResult doPut(String url, Object content) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                        .content(OBJECT_MAPPER.writeValueAsBytes(content))
        ).andReturn();
    }

    protected void assertMvcStatus(HttpStatus status, MvcResult mvcResult) {
        assertEquals(status.value(), mvcResult.getResponse().getStatus());
    }

    protected void assertMvcError(ApplicationExceptionCode code, MvcResult mvcResult) throws Exception {
        assertMvcStatus(code.status, mvcResult);

        ResponseDTO<?> responseDTO = OBJECT_MAPPER.readValue(
                mvcResult.getResponse().getContentAsString(),
                ResponseDTO.class
        );

        assertEquals(code.value, responseDTO.getCode());
    }

    protected <T> List<T> unmarshalListJson(MvcResult from, Class<T> clazz) throws Exception {
        ResponseDTO<List<T>> responseDTO = OBJECT_MAPPER.readValue(
                from.getResponse().getContentAsString(),
                OBJECT_MAPPER.getTypeFactory().constructParametricType(ResponseDTO.class,
                        OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz)
                )
        );

        return responseDTO.getPayload();
    }

    protected <T> T unmarshalJson(MvcResult from, Class<T> clazz) throws Exception {
        ResponseDTO<T> responseDTO = OBJECT_MAPPER.readValue(
                from.getResponse().getContentAsString(),
                OBJECT_MAPPER.getTypeFactory().constructParametricType(ResponseDTO.class, clazz)
        );

        return responseDTO.getPayload();
    }
}
