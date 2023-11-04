package de.marshal.bankapp.exception;

public class ProductNotFoundException extends ApplicationException {
    public ProductNotFoundException(String message) {
        super(PRODUCT_NOT_FOUND, message);
    }
}
