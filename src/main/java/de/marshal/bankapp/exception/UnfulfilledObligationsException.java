package de.marshal.bankapp.exception;

import static de.marshal.bankapp.exception.ApplicationExceptionCode.UNFULFILLED_OBLIGATIONS;

public class UnfulfilledObligationsException extends ApplicationException {
    public UnfulfilledObligationsException(String message) {
        super(UNFULFILLED_OBLIGATIONS, message);
    }
}
