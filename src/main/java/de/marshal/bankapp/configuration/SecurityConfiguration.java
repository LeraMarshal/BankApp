package de.marshal.bankapp.configuration;

import de.marshal.bankapp.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable) // Отключает базовую аутентификацию HTTP, поскольку используем JWT токены
                .csrf(AbstractHttpConfigurer::disable) // Отключает CSRF (Cross-Site Request Forgery) защиту
                .logout(AbstractHttpConfigurer::disable) // Отключает выход из системы
                .sessionManagement(AbstractHttpConfigurer::disable) // Отключает управление сеансами
                .headers(AbstractHttpConfigurer::disable) // Отключает управление заголовками
                .requestCache(AbstractHttpConfigurer::disable) // Отключает кеширование запросов
                .authorizeHttpRequests(authz -> authz
                        //Разрешает не аутентифицированным пользователям делать запросы к swagger-ui
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/**").permitAll()
                        // Остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
