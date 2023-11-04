package de.marshal.bankapp.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter extends HttpFilter {
    public static final String REQUEST_ID_ATTRIBUTE = "RequestId";

    public static String getRequestId(HttpServletRequest request) {
        return request.getAttribute(REQUEST_ID_ATTRIBUTE).toString();
    }

    @Override
    protected void doFilter(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws IOException, ServletException {
        // Собираем request uri для логирования.
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        if (StringUtils.hasText(queryString)) {
            requestURI += "?" + queryString;
        }

        // Выставляем request id.
        String requestId = request.getHeader(REQUEST_ID_ATTRIBUTE);
        if (!StringUtils.hasText(requestId)) {
            requestId = UUID.randomUUID().toString();
        }

        // Сохраняем его в аттрибуте, чтобы его могли использовать в дальнейшем.
        request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);

        try {
            MDC.put(REQUEST_ID_ATTRIBUTE, requestId);
            log.info("Request: method=[{}], from=[{}:{}], uri=[{}]",
                    request.getMethod(),
                    request.getRemoteHost(),
                    request.getRemotePort(),
                    requestURI);

            chain.doFilter(request, response);

            log.info("Response: status=[{}]", response.getStatus());
        } catch (Throwable t) {
            log.error("Response: status=[{}]", response.getStatus(), t);
        } finally {
            MDC.remove(REQUEST_ID_ATTRIBUTE);
        }
    }
}
