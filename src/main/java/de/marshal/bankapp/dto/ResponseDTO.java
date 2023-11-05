package de.marshal.bankapp.dto;

import de.marshal.bankapp.exception.ApplicationException;
import de.marshal.bankapp.exception.ApplicationExceptionCode;
import de.marshal.bankapp.exception.UnspecifiedServerException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO<T> {
    public static <T> ResponseDTO<T> ok(T payload) {
        return new ResponseDTO<>(0, null, payload);
    }

    public static <T> ResponseDTO<T> error() {
        UnspecifiedServerException ex = new UnspecifiedServerException("unspecified server exception");

        return new ResponseDTO<>(ex.getCode().value, ex.getMessage(), null);
    }

    public static <T> ResponseDTO<T> constraintViolation() {
        return new ResponseDTO<>(ApplicationExceptionCode.CONSTRAINT_VIOLATION.value, "constraint violation", null);
    }

    public static <T> ResponseDTO<T> error(ApplicationException ex) {
        return new ResponseDTO<>(ex.getCode().value, ex.getMessage(), null);
    }

    private int code;
    private String message;
    private T payload;

    public ResponseEntity<ResponseDTO<?>> entity() {
        HttpStatus status;

        if (code == 0) {
            status = HttpStatus.OK;
        } else if (code > 0) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(status).body(this);
    }
}
