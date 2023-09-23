package de.marshal.bankapp;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

// https://spring.io/blog/2023/06/23/improved-testcontainers-support-in-spring-boot-3-1
public class AppDev {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "test");

        SpringApplication.from(App::main)
                .with(ContainersConfiguration.class)
                .run(args);
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class ContainersConfiguration {
        @Bean
        @ServiceConnection
        @SuppressWarnings("resource") // отключение предупреждения try-with-resources
        PostgreSQLContainer<?> postgreSQLContainer() {
            return new PostgreSQLContainer<>("postgres:16.0")
                    .withUsername("postgres")
                    .withPassword("admin")
                    .withDatabaseName("bankapp")
                    .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                            // https://stackoverflow.com/questions/69132686/how-can-i-set-the-port-for-postgresql-when-using-testcontainers
                             new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)))
                    ));
        }
    }
}
