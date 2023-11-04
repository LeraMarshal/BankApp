package de.marshal.bankapp.exception;

public class InsufficientFundsException extends ApplicationException {
    public InsufficientFundsException(String message) {
        super(INSUFFICIENT_FUNDS, message);
    }
}
