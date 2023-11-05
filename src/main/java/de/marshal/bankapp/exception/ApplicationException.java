package de.marshal.bankapp.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends Exception {
    private final ApplicationExceptionCode code;

    public ApplicationException(ApplicationExceptionCode code, String message) {
        super(message);

        this.code = code;
    }
}
