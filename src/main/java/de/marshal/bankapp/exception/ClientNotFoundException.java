package de.marshal.bankapp.exception;

import static de.marshal.bankapp.exception.ApplicationExceptionCode.CLIENT_NOT_FOUND;

public class ClientNotFoundException extends ApplicationException {
    public ClientNotFoundException(String message) {
        super(CLIENT_NOT_FOUND, message);
    }
}
