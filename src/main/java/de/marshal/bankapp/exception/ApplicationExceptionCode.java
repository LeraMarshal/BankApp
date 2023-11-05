package de.marshal.bankapp.exception;

import org.springframework.http.HttpStatus;

public enum ApplicationExceptionCode {
    // Ошибки сервера < 0
    UNSPECIFIED(-1),

    // Ошибки клиента > 0
    ACCOUNT_NOT_FOUND(1),
    CLIENT_NOT_FOUND(2),
    PRODUCT_NOT_FOUND(3),
    CURRENCY_CODE_MISMATCH(4),
    INSUFFICIENT_FUNDS(5),
    ILLEGAL_SEARCH_PARAMS(6),
    ;

    public final int value;
    public final HttpStatus status;

    ApplicationExceptionCode(int value) {
        this.value = value;

        if (value == 0) {
            this.status = HttpStatus.OK;
        } else if (value < 0) {
            this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            this.status = HttpStatus.BAD_REQUEST;
        }
    }
}
