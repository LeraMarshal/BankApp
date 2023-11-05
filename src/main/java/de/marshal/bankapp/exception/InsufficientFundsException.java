package de.marshal.bankapp.exception;

import static de.marshal.bankapp.exception.ApplicationExceptionCode.INSUFFICIENT_FUNDS;

public class InsufficientFundsException extends ApplicationException {
    public InsufficientFundsException(String message) {
        super(INSUFFICIENT_FUNDS, message);
    }
}
