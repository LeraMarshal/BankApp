package de.marshal.bankapp.advice;

import de.marshal.bankapp.dto.ExceptionDTO;
import de.marshal.bankapp.interceptor.LoggingInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionDTO> applicationErrorHandler(HttpServletRequest request, ResponseStatusException ex) {
        Throwable cause = ex.getCause();
        String reason = cause == null ? ex.getMessage() : cause.getMessage();

        log.warn("Failed to handle request, id=[{}], reason=[{}]", request.getAttribute(LoggingInterceptor.REQUEST_ID_ATTRIBUTE), reason);

        return ResponseEntity.status(ex.getStatusCode()).body(new ExceptionDTO(reason));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> defaultErrorHandler(HttpServletRequest request, Exception ex) {
        log.error("Failed to handle request, id=[{}]", request.getAttribute(LoggingInterceptor.REQUEST_ID_ATTRIBUTE), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionDTO(ex.getMessage()));
    }
}
