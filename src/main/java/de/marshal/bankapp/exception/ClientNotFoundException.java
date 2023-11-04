package de.marshal.bankapp.exception;

public class ClientNotFoundException extends ApplicationException {
    public ClientNotFoundException(String message) {
        super(CLIENT_NOT_FOUND, message);
    }
}
