package de.marshal.bankapp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {
    public static final String REQUEST_ID_ATTRIBUTE = "RequestId";

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String requestId = UUID.randomUUID().toString();

        request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);

        log.info("Request: id=[{}], method=[{}], from=[{}:{}], uri=[{}?{}]", requestId, request.getMethod(), request.getRemoteHost(), request.getRemotePort(), request.getRequestURI(), request.getQueryString());
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        String requestId = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE);

        if (ex != null) {
            log.error("Response: id=[{}], status=[{}]", requestId, response.getStatus(), ex);
        } else {
            log.info("Response: id=[{}], status=[{}]", requestId, response.getStatus());
        }
    }
}
