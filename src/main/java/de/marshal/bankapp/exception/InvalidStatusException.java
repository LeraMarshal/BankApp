package de.marshal.bankapp.exception;

import static de.marshal.bankapp.exception.ApplicationExceptionCode.INVALID_STATUS;

public class InvalidStatusException extends ApplicationException {
    public InvalidStatusException(String message) {
        super(INVALID_STATUS, message);
    }
}
