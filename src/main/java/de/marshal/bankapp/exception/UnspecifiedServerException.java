package de.marshal.bankapp.exception;

public class UnspecifiedServerException extends ApplicationException {
    public UnspecifiedServerException(String message) {
        super(UNSPECIFIED, message);
    }
}
