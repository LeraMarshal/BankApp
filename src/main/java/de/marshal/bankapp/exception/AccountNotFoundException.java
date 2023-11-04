package de.marshal.bankapp.exception;

public class AccountNotFoundException extends ApplicationException {
    public AccountNotFoundException(String message) {
        super(ACCOUNT_NOT_FOUND, message);
    }
}
