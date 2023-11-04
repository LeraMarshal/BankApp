package de.marshal.bankapp.dto;

import de.marshal.bankapp.exception.ApplicationException;
import de.marshal.bankapp.exception.UnspecifiedServerException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDTO {
    public static ResponseEntity<ExceptionDTO> unspecified() {
        UnspecifiedServerException ex = new UnspecifiedServerException("unspecified server exception");

        return new ExceptionDTO(ex.getCode(), ex.getMessage()).responseEntity();
    }

    public static ResponseEntity<ExceptionDTO> from(ApplicationException ex) {
        return new ExceptionDTO(ex.getCode(), ex.getMessage()).responseEntity();
    }

    private int code;
    private String message;

    public ResponseEntity<ExceptionDTO> responseEntity() {
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
