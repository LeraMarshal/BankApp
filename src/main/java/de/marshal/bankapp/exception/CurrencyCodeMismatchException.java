package de.marshal.bankapp.exception;

import static de.marshal.bankapp.exception.ApplicationExceptionCode.CURRENCY_CODE_MISMATCH;

public class CurrencyCodeMismatchException extends ApplicationException {
    public CurrencyCodeMismatchException(String message) {
        super(CURRENCY_CODE_MISMATCH, message);
    }
}
