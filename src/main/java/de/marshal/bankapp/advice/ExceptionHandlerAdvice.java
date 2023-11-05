package de.marshal.bankapp.advice;

import de.marshal.bankapp.dto.ResponseDTO;
import de.marshal.bankapp.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO<?>> constraintViolationErrorHandler(HttpServletRequest request, DataIntegrityViolationException ex) {
        log.warn("Failed to handle request, exceptionMsg=[{}]", ex.getMessage());

        return ResponseDTO.constraintViolation().entity();
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ResponseDTO<?>> applicationErrorHandler(HttpServletRequest request, ApplicationException ex) {
        log.warn("Failed to handle request, exceptionMsg=[{}]", ex.getMessage());

        return ResponseDTO.error(ex).entity();
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ResponseDTO<?>> defaultErrorHandler(HttpServletRequest request, Throwable t) {
        log.error("Exception caught handling request", t);

        return ResponseDTO.error().entity();
    }
}
