package de.marshal.bankapp.exception;

import static de.marshal.bankapp.exception.ApplicationExceptionCode.PRODUCT_NOT_FOUND;

public class ProductNotFoundException extends ApplicationException {
    public ProductNotFoundException(String message) {
        super(PRODUCT_NOT_FOUND, message);
    }
}
