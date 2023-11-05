package de.marshal.bankapp.exception;

import static de.marshal.bankapp.exception.ApplicationExceptionCode.ACCOUNT_NOT_FOUND;

public class AccountNotFoundException extends ApplicationException {
    public AccountNotFoundException(String message) {
        super(ACCOUNT_NOT_FOUND, message);
    }
}
