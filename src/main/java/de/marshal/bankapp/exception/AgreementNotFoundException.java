package de.marshal.bankapp.exception;

import static de.marshal.bankapp.exception.ApplicationExceptionCode.AGREEMENT_NOT_FOUND;

public class AgreementNotFoundException extends ApplicationException {
    public AgreementNotFoundException(String message) {
        super(AGREEMENT_NOT_FOUND, message);
    }
}
