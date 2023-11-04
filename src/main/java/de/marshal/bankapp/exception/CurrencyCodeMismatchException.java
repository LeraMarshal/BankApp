package de.marshal.bankapp.exception;

public class CurrencyCodeMismatchException extends ApplicationException {
    public CurrencyCodeMismatchException(String message) {
        super(CURRENCY_CODE_MISMATCH, message);
    }
}
