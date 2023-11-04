package de.marshal.bankapp.exception;

public class IllegalSearchParamsException extends ApplicationException {
    public IllegalSearchParamsException(String message) {
        super(ILLEGAL_SEARCH_PARAMS, message);
    }
}
