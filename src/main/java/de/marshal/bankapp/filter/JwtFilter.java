package de.marshal.bankapp.filter;

import de.marshal.bankapp.authentication.JwtAuthentication;
import de.marshal.bankapp.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final String BEARER_AUTHENTICATION_TYPE = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String token = getTokenFromRequest(request);

        if (token != null && jwtService.validateToken(token)) {
            final Claims claims = jwtService.getClaims(token);
            final JwtAuthentication jwtAuthentication = JwtAuthentication.from(claims);

            // Устанавливается аутентификация в контексте безопасности Spring
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        }
        // Передача запроса дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER_AUTHENTICATION_TYPE)) {
            return authorizationHeader.substring(BEARER_AUTHENTICATION_TYPE.length());
        }

        return null;
    }
}
