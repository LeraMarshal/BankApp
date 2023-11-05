package de.marshal.bankapp.exception;

import static de.marshal.bankapp.exception.ApplicationExceptionCode.ILLEGAL_SEARCH_PARAMS;

public class IllegalSearchParamsException extends ApplicationException {
    public IllegalSearchParamsException(String message) {
        super(ILLEGAL_SEARCH_PARAMS, message);
    }
}
