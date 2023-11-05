package de.marshal.bankapp.exception;

import static de.marshal.bankapp.exception.ApplicationExceptionCode.INVALID_INTEREST_RATE;

public class InvalidInterestRateException extends ApplicationException {
    public InvalidInterestRateException(String message) {
        super(INVALID_INTEREST_RATE, message);
    }
}
