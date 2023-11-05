package de.marshal.bankapp.exception;

import static de.marshal.bankapp.exception.ApplicationExceptionCode.UNSPECIFIED;

public class UnspecifiedServerException extends ApplicationException {
    public UnspecifiedServerException(String message) {
        super(UNSPECIFIED, message);
    }
}
