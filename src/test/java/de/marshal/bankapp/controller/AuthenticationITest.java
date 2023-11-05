package de.marshal.bankapp.controller;

import de.marshal.bankapp.AppITests;
import de.marshal.bankapp.configuration.BankConfigurationProperties;
import de.marshal.bankapp.dto.account.AccountDTO;
import de.marshal.bankapp.dto.account.CreateAccountDTO;
import de.marshal.bankapp.entity.AccountStatus;
import de.marshal.bankapp.exception.ApplicationExceptionCode;
import de.marshal.bankapp.repository.AccountRepository;
import de.marshal.bankapp.repository.ClientRepository;
import de.marshal.bankapp.repository.ProductRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext
@AutoConfigureMockMvc
public class AuthenticationITest extends AppITests {
    @Value("${jwt.secret}")
    String jwtSecret;

    @Test
    public void forbiddenWhenNoTokenTest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/test")
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void forbiddenWhenExpiredTokenTest() throws Exception {
        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        String token = Jwts.builder()
                .subject("Bank subsystem")
                .expiration(new Date())
                .signWith(secretKey)
                .compact();

        doGetWithToken(token);
    }

    @Test
    public void forbiddenWhenInvalidTokenTest() throws Exception {
        SecretKey secretKey = Jwts.SIG.HS512.key().build();

        String token = Jwts.builder()
                .subject("Bank subsystem")
                .expiration(new Date(Long.MAX_VALUE))
                .signWith(secretKey)
                .compact();

        doGetWithToken(token);
    }

    private void doGetWithToken(String token) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/test")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
