package de.marshal.bankapp.exception;

public class CurrencyCodeMismatchException extends Exception {
    public CurrencyCodeMismatchException() {
        super("Currency codes mismatch");
    }
}
