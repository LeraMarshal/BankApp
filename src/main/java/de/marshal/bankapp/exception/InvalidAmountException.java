package de.marshal.bankapp.exception;

import static de.marshal.bankapp.exception.ApplicationExceptionCode.INVALID_AMOUNT;

public class InvalidAmountException extends ApplicationException {
    public InvalidAmountException(String message) {
        super(INVALID_AMOUNT, message);
    }
}
