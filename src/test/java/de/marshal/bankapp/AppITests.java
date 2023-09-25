package de.marshal.bankapp;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// https://spring.io/blog/2023/06/23/improved-testcontainers-support-in-spring-boot-3-1
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class AppITests {
    @Container // запускает контейнер
    @ServiceConnection // выставляет свойства datasource
    // https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");
}
