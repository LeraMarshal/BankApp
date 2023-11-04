package de.marshal.bankapp.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends Exception {
    // Ошибки сервера < 0
    public static final int UNSPECIFIED = -1;

    // Ошибки клиента > 0
    public static final int ACCOUNT_NOT_FOUND = 1;
    public static final int CLIENT_NOT_FOUND = 2;
    public static final int PRODUCT_NOT_FOUND = 3;
    public static final int CURRENCY_CODE_MISMATCH = 3;
    public static final int INSUFFICIENT_FUNDS = 4;
    public static final int ILLEGAL_SEARCH_PARAMS = 5;

    private final int code;

    public ApplicationException(int code, String message) {
        super(message);

        this.code = code;
    }
}
